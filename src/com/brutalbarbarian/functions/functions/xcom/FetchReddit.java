package com.brutalbarbarian.functions.functions.xcom;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.XcomFunction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FetchReddit implements XcomFunction {
    private boolean requestStop = false;

    @Override
    public String getName() {
        return "Fetch Reddit Names";
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.Subreddit, Parameter.PageCount);
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch(parameter) {
            case PageCount: return 100;
            case Subreddit: return "xcom";
            default: return null;
        }
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text) {
        requestStop = false;

        ArrayList<String> names = new ArrayList<String>();
        HashMap<String, Integer> posts = new HashMap<String, Integer>();
        HashMap<String, Integer> replies = new HashMap<String, Integer>();
        try {
            String after = "";
            int pageCount = Integer.parseInt(parameters.get(Parameter.PageCount));
            for (int i = 0; i < pageCount; i++) {
                try {
                    String resp = readURL("http://www.reddit.com/r/" + parameters.get(Parameter.Subreddit) +
                            "/new.json?sort=new" + (after.equals("") ? "" : "&after=" + after));
                    if (resp == null || resp.length() == 0) return null; // bad things

                    readName(resp, names, posts);   // Read Names

                    boolean first = true;
                    for (String post : resp.split("\"url\"")) {
                        if (!first) {
                            // Now lets' dig into the URL?
                            String url = post.split("\"")[1];
                            if (url.startsWith("http://www.reddit.com/")) {
                                String htmlResp = readURL(url+"/.json?");
                                if (htmlResp == null || htmlResp.length() == 0) return null; // bad things

                                readName(htmlResp, names, replies); // read replies
                            }
                        }
                        first = false;
                    }

                    after = resp.split("\"after\"")[1].split("\"")[1];
                    if (after.trim().length() == 0) {
                        return null; // null probably....
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Done");

        } finally {
            StringBuffer buffer = new StringBuffer();
            for (String name : names) {
                buffer.append(name + "," + posts.getOrDefault(name, 0) + "," + replies.getOrDefault(name, 0));
                buffer.append("\n");
            }
            return buffer.toString();
        }
    }

    private void readName(String in, List<String> names, HashMap<String, Integer> map) {
        boolean first = true;
        for (String line : in.split("\"author\"")) {
            if (!first) {
                String name = line.split("\"")[1];
                if (!names.contains(name)) {
                    names.add(name);
                }
                map.put(name, map.getOrDefault(name, 0) + 1);
            }
            first = false;
        }
    }

    private String readURL(String urlStr) throws Exception{
        URLConnection con = null;
        //Get Response
        boolean success = false;
        InputStream is = null;
        Integer attempts = 0;
        while (!success) {
            if (requestStop) {
                System.out.println("Received Stop Signal");
                return "";
            }

            try {
                System.out.println("Attempting Connection (" + attempts + ")");
                URL url = new URL(urlStr);
                System.out.println("Calling: " + urlStr);
                con = (URLConnection)url.openConnection();
                is = con.getInputStream();
                success = true;
            } catch (Exception e) {
                if (attempts >  20) {
                    System.out.println("Timed out...");
                    return "";
                }
                attempts++;
                // Reddit only allows one call to same URL per 31 seconds...
                System.out.println("Failed... retrying in 31 seconds");
                Thread.sleep(31000);
            }
        }

        System.out.println("Reading...");
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();

        return response.toString();
    }

    @Override
    public void requestStop() {
        requestStop = true;
    }
}
