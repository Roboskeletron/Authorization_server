package com.roboskeletron.authentication_server.util;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SetMapper {
    public static <T> Set<T> mapFromStrings(Function<String, T> stringConsumer, String... strings){
        return Arrays.stream(strings).map(stringConsumer).collect(Collectors.toSet());
    }

    public static <T> String[] mapToString(Function<T, String> stringFunction, Set<T> set){
        return set.stream().map(stringFunction).toList().toArray(new String[0]);
    }

    public static <T, O> Set<T> mapObjectToSet(Function<O, String> stringFunction,
                                               Function<String, T> objectFunction, Set<O> set){
        return mapFromStrings(objectFunction, mapToString(stringFunction, set));
    }

    public static <T, O> Consumer<Set<T>> getSetStringConsumer(ActionType actionType, Function<O, String> stringFunction,
            Function<String, T> objectFunc, Set<O> set){
        Set<T> tSet = mapObjectToSet(stringFunction, objectFunc, set);
        switch (actionType){
            case ADD -> {
                return items -> items.addAll(tSet);
            }
            case REMOVE -> {
                return items -> items.removeAll(tSet);
            }
        }

        throw new RuntimeException();
    }

    public enum ActionType {
        ADD,
        REMOVE,
    }
}
