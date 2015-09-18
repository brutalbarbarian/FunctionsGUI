package com.brutalbarbarian.functions.functions.smartbuilder;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.SmartBuilderFunction;
import com.brutalbarbarian.utils.IOUtils;
import javafx.scene.control.TextArea;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 16/09/2015.
 */
public class CSVScriptGenerator implements SmartBuilderFunction {
    @Override
    public String getName() {
        return "CSV Script Generator";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text, TextArea outputArea) {
        String csvFileName = parameters.get(Parameter.CSVFile);
        List<String> lines = IOUtils.readAllLines(
                Paths.get(csvFileName), IOUtils.CHARSET_DEFAULT_WINDOWS, IOUtils.CHARSET_UTF8);
        if (lines.size() == 0) {
            return null; // empty file...
        }

        List<String> columns = new ArrayList<>();
        List<String> refColumns = new ArrayList<>();
        List<Integer> refColumnIndexes = new ArrayList<>();

        // Read in the column headers
        columns = Arrays.asList(lines.get(0).split(","));

        // Find all columns referenced in script stub
        String token = "";
        boolean inToken = false;
        char tokenBreak = '%';
        for (char c : text.toCharArray()) {
            if (inToken) {
                if (c == tokenBreak) {
                    if (!refColumns.contains(token)) {
                        refColumns.add(token);
                        int index = columns.indexOf(token);
                        if (index == -1) {
                            System.out.println("Cannot find referenced column " + token + " in csv column headers");
                            return null;
                        }
                        refColumnIndexes.add(index);
                    }
                    inToken = false;
                } else {
                    token = token + c;
                }
            } else if (c == tokenBreak) {
                token = "";
                inToken = true;
            }
        }

        // Remove the column header line
        lines.remove(0);

        StringBuilder out = new StringBuilder();

        for (String line : lines) {
            String[] lineValues = line.split(",");

            String scriptStub = text;
            for (int i = 0; i < refColumns.size(); i++) {
                String value = lineValues[refColumnIndexes.get(i)];
                scriptStub = scriptStub.replaceAll('%' + refColumns.get(i) + '%', value);
            }

            out.append(scriptStub).append("\n");
        }

        return out.toString();
    }

    @Override
    public boolean requiresText() {
        return true;
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.CSVFile);
    }
}
