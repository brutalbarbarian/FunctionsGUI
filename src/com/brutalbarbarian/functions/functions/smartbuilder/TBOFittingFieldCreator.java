package com.brutalbarbarian.functions.functions.smartbuilder;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.SmartBuilderFunction;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Lu on 28/08/2015.
 */
public class TBOFittingFieldCreator implements SmartBuilderFunction {
    @Override
    public String getName() {
        return "TBOFitting Field Creator";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text, TextArea outputArea) {
        Path path = Paths.get(parameters.get(Parameter.TBOFittingPath));
        try {
            List<String> lines = Files.readAllLines(path, Charset.forName("windows-1258"));
            for (int i = 0; i < lines.size(); i++) {
                lines.set(i, fixString(lines.get(i)));
            }

            // Build up a list of all existing classes we need to check
            List<String> classes = new ArrayList<>();
            List<String> affectedClasses = new ArrayList<>();
            // Does not add self when going up the tree
            addClasses(parameters.get(Parameter.FittingClass), lines, classes, true);
            addClasses(parameters.get(Parameter.FittingClass), lines, affectedClasses, false);
            classes.addAll(affectedClasses);

            String type = parameters.get(Parameter.FieldType);

            // For each class... build up the fields of the same field type
            List<Integer> fieldsTaken = new ArrayList<>();
            for (String className : classes) {
                addFieldsOfType(type, className, lines, fieldsTaken, false);
            }

            fieldsTaken.sort(Comparator.<Integer>naturalOrder());
            int suffix = 1;
            for (int i : fieldsTaken) {
                suffix = i + 1;
                if (!fieldsTaken.contains(suffix)) {
                    break;  // We found a free one.
                }
            }

            // Now lets print some results
            String attributeName = parameters.get(Parameter.AttributeName);
            String fullTypeName = getTypeFullName(type, parameters.get(Parameter.IsLength));

            StringBuffer result = new StringBuffer();
            result.append("F" + attributeName + " : TDbbBOAttribute" + fullTypeName + ";").append("\n");
            result.append("property " + attributeName + " : TDbbBOAttribute" + fullTypeName + " read F" + attributeName + ";").append("\n");;
            result.append("F" + attributeName + " := CreateAttribute" + fullTypeName + "('" + attributeName + "', '" + getFieldPrefix(type) + suffix+"');").append("\n");;

            result.append("Affected Classes:").append("\n");
            for (String className : affectedClasses) {
                result.append(className).append("\n");
            }

            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String fixString(String input) {
        StringBuffer sb = new StringBuffer(input.length());
        boolean hasWhiteSpaceAdded = false;
        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                if (hasWhiteSpaceAdded) {
                    // Do nothing
                } else {
                    sb.append(" ");
                    hasWhiteSpaceAdded = true;
                }
            } else {
                sb.append(c);
                hasWhiteSpaceAdded = false;
            }
        }
        return sb.toString();
    }

    private String getTypeFullName(String type, String isLength) {
        switch(type) {
            case "int" :
                if (Boolean.parseBoolean(isLength)) {
                    return "Length";
                } else {
                    return "Integer";
                }
            case "string" :
                return "String";
            case "bool" :
                return "Boolean";
            case "clr" :
                return "Integer";
            case "int64" :
                return "Int64";
            case "real" :
                return "Double";
            case "numeric" :
                return "Double";
            case "img" :
                return "Integer";
        }
        return "";
    }

    private String getFieldPrefix(String type) {
        String typeSuffix = "fdt_" + type + "_";
        if (type.equals("clr")) {
            typeSuffix = "clr_id_";
        } else if (type.equals("img")) {
            typeSuffix = "img_id_";
        }
        return typeSuffix;
    }

    private void addFieldsOfType(String type, String className, List<String> lines, List<Integer> fieldsTaken, boolean isCommon) {
//        System.out.println(className);
        // find the start of the create attributes of class. ignore a few though.
        boolean foundCreateAttributeStart = false;
        boolean foundCommonStorageStart = false;
        String startCreateSuffix = className;
        if (isCommon) {
            startCreateSuffix += ".CreateCommonAttributes";
        } else {
            startCreateSuffix += ".CreateAttributes";
        }
        startCreateSuffix = startCreateSuffix.toLowerCase();
        String startCommonSuffix = (className + ".CommonAttributeStorage").toLowerCase();
        String typeSuffix = getFieldPrefix(type);
        for (String _line : lines) {
            String line = _line.toLowerCase();
            if (foundCreateAttributeStart) {
                // check if we've hit the end.
                if (line.startsWith("end")) {
                    foundCreateAttributeStart = false;
                } else {
                    // Line isn't commented out
                    if (!line.trim().startsWith("//")) {
                        int startIndex = line.indexOf(typeSuffix);
//                        System.out.println(typeSuffix + "::" + line + "::" + startIndex);
                        if (startIndex >= 0) {
                            startIndex = startIndex + typeSuffix.length();
                            int endIndex = line.indexOf("'", startIndex);
                            String value = line.substring(startIndex, endIndex);
                            int num = Integer.parseInt(value);
                            fieldsTaken.add(num);
                        }
                    }
                }
            } else if (foundCommonStorageStart) {
                if (line.startsWith("end")) {
                    foundCommonStorageStart = false;
                } else {
                    if (!line.trim().startsWith("//")) {
                        // if we found ".Create"... assume we're creating the common storage?
                        int endIndex = line.indexOf(".create");
//                        System.out.println(line + ":" + endIndex);
                        if (endIndex >= 0) {
                            int startIndex = line.indexOf(":=") + (":=").length();
                            String commonStubName = line.substring(startIndex, endIndex).trim();
                            addFieldsOfType(type, commonStubName, lines, fieldsTaken, true);
                        }
                    }
                }
            } else {
                // check if we've found the create attribute start
                foundCreateAttributeStart = line.contains(startCreateSuffix);
                foundCommonStorageStart = line.contains(startCommonSuffix);
            }
        }
    }

    private void addClasses(String _classname, List<String> lines, List<String> classes, boolean goingUp) {
        // we're either going up the tree, or going down the tree...
        String classname = _classname.toLowerCase();
        if (goingUp) {
            String prefix = classname + " = class(";
            // search for the line of the current classname declaration
            for (String _line : lines) {
                String line = _line.toLowerCase();

                int startIndex = line.indexOf(prefix);
                if (startIndex >= 0) {
                    startIndex = startIndex + prefix.length();
                    // Found the class declaration
                    int endIndex = line.indexOf(")", startIndex);
                    if (line.indexOf(",") > 0) {
                        endIndex = line.indexOf(",");
                    }
                    String superClassName = _line.substring(startIndex, endIndex);
                    if (!superClassName.equalsIgnoreCase("TBOFittingDetail")) {
                        // We don't want to go beyond this
                        classes.add(superClassName);

                        // recurse on the superclass
                        addClasses(superClassName, lines, classes, goingUp);
                    }
                    break;
                }
            }
        } else {
            classes.add(_classname);

            String suffix = " = class(" + classname;
            for (String _line : lines) {
                String line = _line.toLowerCase();
                int endIndex = line.indexOf(suffix + ")");
                if (endIndex < 0) {
                    endIndex = line.indexOf(suffix + ",");
                }
                if (endIndex >= 0) {
                    // Found a child class
                    String childClassName = _line.substring(0, endIndex).trim();
                    addClasses(childClassName, lines, classes, goingUp);
                }
            }
        }
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(Parameter.TBOFittingPath, Parameter.AttributeName, Parameter.FieldType, Parameter.FittingClass,
                Parameter.IsLength);
    }

    @Override
    public Object getDefaultParamValue(Parameter parameter) {
        switch(parameter) {
            case TBOFittingPath:
                String path = System.getenv("DBS_VCL"); //return "C:\\DBS\\Dev\\Main\\Project\\Common\\BOFitting.pas";
                int index = path.indexOf("\\VCL");
                path = path.substring(0, index);
                return path + "\\Project\\Common\\BOFitting.pas";
            case FieldType: return "int";   // int, clr, bool, str, etc
            case IsLength: return "False";
            default: return null;
        }
    }
}
