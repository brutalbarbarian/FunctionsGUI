package com.brutalbarbarian.functions.functions.xcom;

import com.brutalbarbarian.functions.constants.Parameter;
import com.brutalbarbarian.functions.interfaces.XcomFunction;
import javafx.scene.control.TextArea;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Lu
 * Date: 23/08/15
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class RandomPerkGenerator implements XcomFunction{
    @Override
    public String getName() {
        return "Random Perk Generator";
    }

    @Override
    public Object computeResult(HashMap<Parameter, String> parameters, String text, TextArea outputArea) {
        List<String> in = Arrays.asList(text.split("\n"));
        Set<String> perks = new HashSet<String>();
        for (String line : in) {
            int index = -1;
            while((index = line.indexOf("ePerk_", index + 1)) > -1) {
                String perk = "";
                char nextChar;
                do {
                    nextChar = line.charAt(index);
                    if (Character.isAlphabetic(nextChar) ||
                            Character.isDigit(nextChar) ||
                            nextChar == '_') {
                        perk += nextChar;
                        index++;
                    } else {
                        break;
                    }
                }while (index < line.length());
                perks.add(perk);
            }
        }


        List<String> perksSorted = new ArrayList<String>(perks);
        perksSorted.sort(Comparator.<String>naturalOrder());

        StringBuffer buffer = new StringBuffer();
        for (String perk : perksSorted) {
            buffer.append("RandomPerks=" + perk);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    @Override
    public boolean requiresText() {
        return true;
    }
}
