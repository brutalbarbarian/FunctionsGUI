package com.brutalbarbarian.functions.functions.xcom;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.constants.ResultType;
import com.brutalbarbarian.functions.interfaces.XcomFunction;
import com.sun.glass.ui.Application;
import javafx.scene.control.TextArea;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.NIOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Lu
 * Date: 23/08/15
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SteamOverlaySearch implements XcomFunction {
    @Override
    public String getName() {
        return "Steam Overlay Search";
    }

    private boolean allowRun = false;

    private double computeScore(int r, int g, int b, int _r, int _g, int _b) {
        // Return a percentage of difference.
        return (Math.abs(r - _r)/255.0 + Math.abs(g - _g)/255.0 + Math.abs(b - _b)/255.0) / 3;
    }

    private int clamp(double value, int min, int max) {
        int result = (int) Math.round(value);
        return Math.max(min, Math.min(max, result));
    }

    @Override
    public ResultType getResultType() {
        return ResultType.Ignore;
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text, TextArea outputArea) {
        allowRun = true;
        try {
            BufferedImage base = ImageIO.read(new File(parameters.get(Parameter.LabelRefPath)));
            int xOff = 1105;
            int yOff = 12;
            String file = parameters.get(Parameter.VideoPath);
            int start = 0;
            int finish = Integer.parseInt(parameters.get(Parameter.Duration)) * 60;   // Assume it's in minutes

            FrameGrab grab = new FrameGrab(NIOUtils.readableFileChannel(new File(file)));
            FrameGrab.MediaInfo info = grab.getMediaInfo();

            int width = info.getDim().getWidth();
    //                int height = info.getDim().getHeight();

            double sec = start;
            while (sec <= finish && allowRun) {
                System.out.println("Processing: " + String.format("%.1f/%d", sec, finish));
                grab.seekToSecondSloppy(sec);

                int[][] data = grab.getNativeFrame().getData();
                int[] px = new int[3];

                double score = 0;
                for (int x = 0; x < base.getWidth(); x++) {
                    for (int y = 0; y < base.getHeight(); y++) {
                        int _y = data[0][(y+yOff)*width + (x+xOff)];
                        int _u = data[1][((y+yOff)/2 * width/2) + (x+xOff)/2];
                        int _v = data[2][((y+yOff)/2 * width/2) + (x+xOff)/2];

                        int r = clamp(_y + (1.370705 * (_v - 128)), 0, 255);
                        int g = clamp(_y - (0.698001 * (_v - 128)) - (0.337633 * (_u - 128)), 0, 255);
                        int b = clamp(_y + (1.732446 * (_u - 128)), 0, 255);

                        base.getRaster().getPixel(x, y, px);
                        score += computeScore(r, g, b, px[0], px[1], px[2]);

                    }
                }
                // Get the average overall score.
                score = score / (base.getWidth() * base.getHeight());

                // Assume 0.1 is the threshold to say this is similar enough
                if (score < 0.1) {
                    long seconds = Math.round(Math.floor(sec));
                    long minutes = seconds/60;

                    Application.invokeLater(new AddToList(String.format("%2d:%2d\n", minutes, seconds % 60), outputArea));
                }
                sec += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    class AddToList implements Runnable {
        String itemToAdd;
        TextArea area;

        public AddToList(String time, TextArea area) {
            itemToAdd = time;
            this.area = area;
        }

        @Override
        public void run() {
            area.appendText(itemToAdd);
        }
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.VideoPath, Parameter.LabelRefPath, Parameter.Duration);
    }

    @Override
    public void requestStop() {
        allowRun = false;
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch(parameter) {
            case VideoPath: return "G:\\Youtube Backup\\Original Resource S2";
            case LabelRefPath: return "F:\\Label.png";
            case Duration: return "30";
            default: return null;
        }
    }
}
