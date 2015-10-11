package com.brutalbarbarian.functions.functions.smartbuilder;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.SmartBuilderFunction;
import com.brutalbarbarian.utils.IOUtils;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Lu on 15/09/2015.
 */
public class UpdateFittingLanguage implements SmartBuilderFunction{
    @Override
    public String getName() {
        return "Update Fitting Translations from CSV";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text, TextArea outputArea) {
        String csvFileName = parameters.get(Parameter.CSVFile);
        List<String> lines = IOUtils.readAllLines(
                Paths.get(csvFileName), IOUtils.CHARSET_DEFAULT_WINDOWS, IOUtils.CHARSET_UTF8);
        if (lines.size() == 0) {
            return null; // empty file...
        }

        List<String> columns = Arrays.asList(lines.get(0).split(","));
        int fittingIDIndex = columns.indexOf(parameters.get(Parameter.FittingIDColumnName));
        int fittingNameIndex = columns.indexOf(parameters.get(Parameter.TranslatedFittingNameColumn));
        int fittingDescriptionIndex = columns.indexOf(parameters.get(Parameter.TranslatedDescriptionColumn));
        int manufacturerIDIndex = columns.indexOf(parameters.get(Parameter.ManufacturerIDColumn));

        lines.remove(0);

        List<String> output = new ArrayList<>(lines.size());

        String languageID = parameters.get(Parameter.LanguageID);

        for(String line : lines) {
            String [] values = line.split(",");
            String fittingID = values[fittingIDIndex];
            String fittingName = values[fittingNameIndex];
            String fittingDescription = values[fittingDescriptionIndex];
            String manufacturerID = values[manufacturerIDIndex];

            output.add("delete TL_FIT where fit_id = " + fittingID + " and lng_id = " + languageID);
            output.add("insert TL_FIT (fit_id, lng_id, fit_name, fit_description) values (" + fittingID + "," + languageID + ",'" + fittingName + "','" + fittingDescription +"')");
            output.add("update TR_FIT_fitting set mfr_id = " + manufacturerIDIndex + " where fit_id = " + fittingID);
        }

        StringBuilder result = new StringBuilder();
        for (String line : output) {
            result.append(line).append("\n");
        }

        return result.toString();
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.CSVFile,
                Parameter.FittingIDColumnName,
                Parameter.LanguageID,
                Parameter.TranslatedFittingNameColumn,
                Parameter.TranslatedDescriptionColumn,
                Parameter.ManufacturerIDColumn);
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch(parameter) {
            case FittingIDColumnName: return "fit_id";
            case LanguageID: return "1";
            case TranslatedFittingNameColumn: return "fit_name";
            case TranslatedDescriptionColumn: return "fit_description";
            case ManufacturerIDColumn: return "mfr_id";
            default: return null;
        }
    }
}
