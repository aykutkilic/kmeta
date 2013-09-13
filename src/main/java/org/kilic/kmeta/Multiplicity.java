package org.kilic.kmeta;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.regex.Pattern;

public class Multiplicity {
    private MultiplicityEnum type;
    private int count;

    public void createFromMultiplicityContext(KMetaParser.MultiplicityContext ctx) {
        createFromString(ctx.getText());
    }

    public void createFromString(String input) {
        if (input == null || input.isEmpty()) type = MultiplicityEnum.SINGLE;

        switch (input) {
            case "1":
                type = MultiplicityEnum.SINGLE;
            case "?":
                type = MultiplicityEnum.OPTIONAL;
            case "+":
                type = MultiplicityEnum.ONE_OR_MORE;
            case "*":
                type = MultiplicityEnum.ANY;
        }

        if( input.matches("\\[[0-9]+\\]") ) {
            type = MultiplicityEnum.FIXED;
            count = Integer.valueOf(input.substring(1,input.length()-1));
        } else
            type = MultiplicityEnum.UNKNOWN;
    }

    @Override
    public String toString() {
        switch (type) {
            case SINGLE:
                return "";
            case FIXED:
                return "[" + String.valueOf(count) + "]";
            case OPTIONAL:
                return "?";
            case ONE_OR_MORE:
                return "+";
            case ANY:
                return "*";
        }
        return null;
    }

    public MultiplicityEnum getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

}
