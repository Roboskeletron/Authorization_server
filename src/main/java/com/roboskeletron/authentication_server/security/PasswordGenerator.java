package com.roboskeletron.authentication_server.security;

import java.security.SecureRandom;
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
        String[] patternsArray = patterns.toArray(new String[0]);
        SecureRandom lengthRandom = new SecureRandom();

        int length = lengthRandom.nextInt(minLength, maxLength + 1);

        for (var pattern : patterns){
            passwordBuilder.append(getRandomCharFromPattern(pattern));
        }

        SecureRandom patternsRandom = new SecureRandom();

        while (passwordBuilder.length() < length){
            int index = patternsRandom.nextInt(patternsArray.length);

            var pattern = patternsArray[index];

            passwordBuilder.append(getRandomCharFromPattern(pattern));
        }

        return passwordBuilder.toString();
    }

    private void parseRegex(String regex){
        Pattern charactersPattern = Pattern.compile("\\[(.*?)\\]");
        Pattern limitsPattern = Pattern.compile("\\{(.*?)\\}");
        Pattern rangePattern = Pattern.compile(".-.");

        HashSet<String> characters = new HashSet<>();

        capturePatterns(charactersPattern.matcher(regex), characters);

        addNumbers(regex, characters);

        setLimits(limitsPattern.matcher(regex));

        characters.forEach(group -> addGroup(group, rangePattern));
    }

    private void capturePatterns(Matcher matcher, Set<String> set){
        while (matcher.find()){
            var group = matcher.group();
            set.add(group.substring(1, group.length() - 1));
        }
    }

    private void setLimits(Matcher matcher){
        if (!matcher.find()){
            minLength = 8;
            maxLength = 8;
            return;
        }
        var limit = matcher.group();
        limit = limit.substring(1, limit.length() - 1);

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

    private void addNumbers(String regex, Set<String> set){
        if (regex.contains("\\d"))
            set.add("0-9");
    }

    private char getRandomCharFromPattern(String pattern){
        SecureRandom random = new SecureRandom();
        int index = random.nextInt(pattern.length());

        return pattern.charAt(index);
    }
}
