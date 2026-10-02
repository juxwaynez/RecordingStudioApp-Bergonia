package com.joysistvi.recordingapp.cliview;

import com.joysistvi.recordingapp.controller.UserController;
import com.joysistvi.recordingapp.model.User;

import java.util.List;
import java.util.Scanner;

// Admin-only screen for managing registered accounts.
// Register/Login are handled at the welcome gate in App, not here.
public class UserView {

    private final UserController userController; // Composition
    private final Scanner scanner;

    // Constructor injection
    public UserView(UserController userController, Scanner scanner) {
        this.userController = userController;
        this.scanner = scanner;
    }

    public void run() {
        int choice;
        do {
            clearScreen();
            printMenu();
            choice = promptChoice();

            switch (choice) {
                case 1 -> viewAllUsers();
                case 2 -> deleteUser();
                case 0 -> System.out.println("Returning to dashboard...");
                default -> System.out.println("Invalid choice. Try again.");
            }

            if (choice != 0) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println("\n--- MANAGE USERS --");
        System.out.println("1. View All Users");
        System.out.println("2. Delete User");
        System.out.println("0. Back");
    }

    private int promptChoice() {
        System.out.print("Choice: ");
        return readInt();
    }

    private void viewAllUsers() {
        List<User> users = userController.handleViewAllUsers();
        printUsers(users);
    }

    private void deleteUser() {
        System.out.print("User ID to delete: ");
        int id = readInt();

        boolean success = userController.handleDeleteUser(id);
        System.out.println(success ? "User deleted successfully." : "Failed to delete user.");
    }

    private void printUsers(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        String border = "+" + "-".repeat(6) + "+" + "-".
                repeat(22) + "+" + "-".repeat(10) + "+";

        System.out.println(border);
        System.out.printf("| %-4s | %-20s | %-8s |%n", "ID", "Username", "Role");
        System.out.println(border);

        for (User user : users) {
            System.out.printf("| %-4d | %-20s | %-8s |%n", user.getId(), user.getUsername(), user.getRole());
        }

        System.out.println(border);
    }

    // Clears the console using ANSI escape codes. Works in real terminals and in
    // IntelliJ's Run console IF "Emulate terminal in output console" is enabled.
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Reads an int safely, re-prompting on invalid input, then consumes the trailing newline
    private int readInt() {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline
        return value;
    }
}
