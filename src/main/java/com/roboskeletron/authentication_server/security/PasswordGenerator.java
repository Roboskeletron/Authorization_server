package com.roboskeletron.authentication_server.security;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordGenerator {
    private final Set<String> patterns = new HashSet<>();
    private int minLength = 0;
    private int maxLength = 0;

    public PasswordGenerator(String regex){
        parseRegex(regex);
    }

    public String generate(){
        StringBuilder passwordBuilder = new StringBuilder();

        while (passwordBuilder.length() < minLength){
            if (passwordBuilder.length() > maxLength)
                passwordBuilder.setLength(0);
        }

        return passwordBuilder.toString();
    }

    private void parseRegex(String regex){
        Pattern charactersPattern = Pattern.compile("/\\[(.*?)\\]/gm");
        Pattern limitsPattern = Pattern.compile("/\\{(.*?)\\}/gm");
        Pattern rangePattern = Pattern.compile("/.-./gm");

        HashSet<String> characters = new HashSet<>();

        capturePatterns(charactersPattern.matcher(regex), characters);

        String limits = limitsPattern.matcher(regex).group();
        setLimits(limits);

        characters.forEach(group -> addGroup(group, rangePattern));
    }

    private void capturePatterns(Matcher matcher, Set<String> set){
        while (matcher.find()){
            set.add(matcher.group());
        }
    }

    private void setLimits(String limit){
        var limits = limit.split(",");

        minLength = limits[0].isEmpty() ? 0 : Integer.parseInt(limits[0]);
        maxLength = limits[1].isEmpty() ? minLength : Integer.parseInt(limits[1]);
    }

    private String generateRange(String range){
        char left = range.charAt(0);
        char right = range.charAt(2);

        StringBuilder builder = new StringBuilder();

        for (char i = left; i <= right; i++){
            builder.append(i);
        }

        return builder.toString();
    }

    private void addGroup(String group, Pattern rangePattern){
        Matcher matcher = rangePattern.matcher(group);

        if (matcher.matches()){
            patterns.add(generateRange(group));
            return;
        }

        patterns.add(group);
    }
}
