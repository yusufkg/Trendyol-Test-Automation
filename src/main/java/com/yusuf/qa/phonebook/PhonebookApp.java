package com.yusuf.qa.phonebook;

import java.util.Scanner;

public class PhonebookApp {
    public static void main(String[] args) {
        ContactRepository repo = new ContactRepository();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1 Add | 2 List | 3 Update | 4 Delete | 0 Exit");
                String choice = scanner.nextLine().trim();
                if (choice.equals("0")) break;
                try {
                    switch (choice) {
                        case "1" -> System.out.println("Created ID: " + repo.add(ask(scanner,"First name"), ask(scanner,"Last name"), ask(scanner,"Phone"), ask(scanner,"Email")));
                        case "2" -> repo.list().forEach(System.out::println);
                        case "3" -> System.out.println("Updated: " + repo.update(Integer.parseInt(ask(scanner,"ID")), ask(scanner,"First name"), ask(scanner,"Last name"), ask(scanner,"Phone"), ask(scanner,"Email")));
                        case "4" -> System.out.println("Deleted: " + repo.delete(Integer.parseInt(ask(scanner,"ID"))));
                        default -> System.out.println("Unknown choice");
                    }
                } catch (Exception e) { System.err.println("Operation failed: " + e.getMessage()); }
            }
        }
    }
    private static String ask(Scanner scanner, String label) { System.out.print(label + ": "); return scanner.nextLine(); }
}
