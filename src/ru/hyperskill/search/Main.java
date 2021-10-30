package ru.hyperskill.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final String ENTER_DATA_TO_SEARCH = "\nEnter a name or email to search all suitable people.";
    private static final String NO_MATCH_FOUND = "No matching people found.";
    private static final String FOUND = "\nFound people:";
    private static final String LIST_OF_DATA = "=== List of people ===";
    private static final String SELECT_MATCHING_STRATEGY = "Select a matching strategy: ALL, ANY, NONE";
    private static final String MENU = "\n=== Menu ===\n" +
            "1. Find a person\n" +
            "2. Print all people\n" +
            "0. Exit";
    private static final int SEARCH = 1;
    private static final int PRINT_ALL_DATA = 2;
    private static final int EXIT = 0;

    public static List<String> readData(String filename) {
        File file = new File(filename);
        List<String> arrayList = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                arrayList.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static String getMatchingStrategy(Scanner scanner) {
        switch (scanner.nextLine()) {
            case "ANY":
                return "ANY";
            case "ALL":
                return "ALL";
            case "NONE":
                return "NONE";
            default:
                return null;
        }
    }

    public static boolean searchWithStrategy(List<String> lines, Map<String, List<Integer>> index, String toFind, String strategy) {

        String[] array = toFind.split(" ");
        Set<Integer> set = index.values().stream().flatMap(List::stream).collect(Collectors.toSet());
        switch (strategy) {
            case "ALL":
                for (String s : array) {
                    if (index.containsKey(s) && !set.isEmpty()) {
                        Set<Integer> newSet = new HashSet<>(index.get(s));
                        set.retainAll(newSet);
                    } else {
                        return false;
                    }
                }
                if (set.isEmpty()) {
                    return false;
                }
                System.out.println(FOUND);
                for (Integer i : set) {
                    System.out.println(lines.get(i));
                }
                return true;
            case "ANY":
                set = new HashSet<>();
                for (String s : array) {
                    if (index.containsKey(s)) {
                        Set<Integer> newSet = new HashSet<>(index.get(s));
                        set.addAll(newSet);
                    }
                }
                if (set.isEmpty()) {
                    return false;
                }
                System.out.println(FOUND);
                for (Integer i : set) {
                    System.out.println(lines.get(i));
                }
                return true;
            case "NONE":
                for (String s : array) {
                    if (index.containsKey(s)) {
                        Set<Integer> newSet = new HashSet<>(index.get(s));
                        set.removeAll(newSet);
                    }
                }
                if (set.isEmpty()) {
                    return false;
                }
                System.out.println(FOUND);
                for (Integer i : set) {
                    System.out.println(lines.get(i));
                }
                return true;
            default:
                return false;

        }
    }

    public static void searchInformation(List<String> lines, Scanner scanner, Map<String, List<Integer>> index) {
        System.out.println(SELECT_MATCHING_STRATEGY);
        String strategy = getMatchingStrategy(scanner);
        if (strategy == null) {
            System.err.println("Error: wrong strategy name. Possible values are: ALL, ANY, NONE.");
            return;
        }
        System.out.println(ENTER_DATA_TO_SEARCH);
        String toFind = scanner.nextLine();
        if (!searchWithStrategy(lines, index, toFind.toLowerCase(), strategy)) {
            System.out.println(NO_MATCH_FOUND);
        }
    }

    public static void printAllData(List<String> lines) {
        System.out.println(LIST_OF_DATA);
        for (String s : lines) {
            System.out.println(s);
        }
    }

    public static void runMainLoop(List<String> lines, Map<String, List<Integer>> index) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println(MENU);
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case SEARCH:
                        searchInformation(lines, scanner, index);
                        break;
                    case PRINT_ALL_DATA:
                        printAllData(lines);
                        break;
                    case EXIT:
                        scanner.close();
                        System.out.println("Bye!");
                        System.exit(0);
                    default:
                        System.out.println("Incorrect option! Try again.");
                }
            }
        }
    }

    public static Map<String, String> argumentsToMap(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--data":
                    if (args.length - 1 >= i) {
                        map.put(args[i], args[i + 1]);
                    }
                    break;
                default:
            }
        }
        return map;
    }

    public static Map<String, List<Integer>> createIndex(List<String> lines) {
        Map<String, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < lines.size(); i++) {
            for (String w : lines.get(i).split(" ")) {
                if (!map.containsKey(w.toLowerCase())) {
                    map.put(w.toLowerCase(), new ArrayList<>(Arrays.asList(i)));
                } else {
                    map.get(w.toLowerCase()).add(i);
                }
            }
        }
        return map;
    }

    public static void main(String[] args) {
        Map<String, String> arguments = argumentsToMap(args);
        if (arguments.containsKey("--data")) {
            String filename = arguments.get("--data");
            List<String> lines = readData(filename);
            if (lines.isEmpty()) {
                System.err.println("Error: the data source is empty.");
                System.exit(1);
            }
            Map<String, List<Integer>> index = createIndex(lines);
            runMainLoop(lines, index);
        } else {
            System.err.println("Error: no '--data' source specified.");
            System.exit(1);
        }
    }
}
