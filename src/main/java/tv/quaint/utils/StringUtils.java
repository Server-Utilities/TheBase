package tv.quaint.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static String color(String s) {
        return s.replace("&", "§");
    }

    public static String uncolor(String s) {
        return s.replace("§", "&");
    }

    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String uncapitalize(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    public static String capitalizeAll(String s) {
        String[] split = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String s1 : split) {
            sb.append(capitalize(s1)).append(" ");
        }
        return sb.toString().trim();
    }

    public static String uncapitalizeAll(String s) {
        String[] split = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String s1 : split) {
            sb.append(uncapitalize(s1)).append(" ");
        }
        return sb.toString().trim();
    }

    public static <T> List<String> stringify(List<T> list) {
        List<String> strings = new ArrayList<>();
        for (T t : list) {
            strings.add(t.toString());
        }
        return strings;
    }

    public static <T> T[] argsMinus(T[] array, int... indexes) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            boolean skip = false;
            for (int index : indexes) {
                if (i == index) {
                    skip = true;
                    break;
                }
            }
            if (! skip) {
                list.add(array[i]);
            }
        }
        return list.toArray(array);
    }

    public static String[] argsMinus(String[] array, int... indexes) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            boolean skip = false;
            for (int index : indexes) {
                if (i == index) {
                    skip = true;
                    break;
                }
            }
            if (! skip) {
                list.add(array[i]);
            }
        }
        return list.toArray(array);
    }

    public static <T> String argsToString(T[] args) {
        StringBuilder sb = new StringBuilder();
        for (T arg : args) {
            sb.append(arg).append(" ");
        }
        return sb.toString().trim();
    }

    public static String argsToString(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        return sb.toString().trim();
    }

    public static <T> String argsToStringMinus(T[] args, int... indexes) {
        T[] argsMinus = argsMinus(args, indexes);
        return argsToString(argsMinus);
    }

    public static String argsToStringMinus(String[] args, int... indexes) {
        String[] argsMinus = argsMinus(args, indexes);
        return argsToString(argsMinus);
    }
}
