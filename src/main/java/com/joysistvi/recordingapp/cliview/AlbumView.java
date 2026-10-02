package com.joysistvi.recordingapp.cliview;

import com.joysistvi.recordingapp.controller.AlbumController;
import com.joysistvi.recordingapp.model.Album;

import java.util.List;
import java.util.Scanner;

public class AlbumView {

    private final AlbumController albumController; // Composition
    private final Scanner scanner;
    private final boolean isAdmin;

    // Constructor injection
    public AlbumView(AlbumController albumController, Scanner scanner, boolean isAdmin) {
        this.albumController = albumController;
        this.scanner = scanner;
        this.isAdmin = isAdmin;
    }

    public void run() {
        int choice;
        do {
            clearScreen();
            printMenu();
            choice = promptChoice();

            switch (choice) {
                case 1 -> viewAllAlbums();
                case 2 -> searchAlbum();
                case 3 -> { if (isAdmin) addAlbum(); else denyAccess(); }
                case 4 -> { if (isAdmin) updateAlbum(); else denyAccess(); }
                case 5 -> { if (isAdmin) deleteAlbum(); else denyAccess(); }
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
        System.out.println("\n===== ALBUM CATALOG =====");
        System.out.println("1. View All Albums");
        System.out.println("2. Search Album");
        if (isAdmin) {
            System.out.println("3. Add Album");
            System.out.println("4. Update Album");
            System.out.println("5. Delete Album");
        }
        System.out.println("0. Back");
    }

    private int promptChoice() {
        System.out.print("Choice: ");
        return readInt();
    }

    private void viewAllAlbums() {
        List<Album> albums = albumController.handleViewAllAlbums();
        printAlbums(albums);
    }

    private void searchAlbum() {
        System.out.print("Enter name keyword: ");
        String keyword = scanner.nextLine();
        printAlbums(albumController.handleSearchAlbum(keyword));
    }

    private void addAlbum() {
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Year: ");
        int year = readInt();

        System.out.print("Artist ID: ");
        int artistId = readInt();

        Album album = new Album(name, year, artistId);

        boolean success = albumController.handleAddAlbum(album);
        System.out.println(success ? "Album added successfully." : "Failed to add album.");
    }

    private void updateAlbum() {
        System.out.print("Album ID to update: ");
        int id = readInt();

        System.out.print("New Name: ");
        String name = scanner.nextLine();

        System.out.print("New Year: ");
        int year = readInt();

        System.out.print("New Artist ID: ");
        int artistId = readInt();

        Album album = new Album(id, name, year, artistId);

        boolean success = albumController.handleUpdateAlbum(album);
        System.out.println(success ? "Album updated successfully." : "Failed to update album.");
    }

    private void deleteAlbum() {
        System.out.print("Album ID to delete: ");
        int id = readInt();

        boolean success = albumController.handleDeleteAlbum(id);
        System.out.println(success ? "Album deleted successfully." : "Failed to delete album.");
    }

    private void printAlbums(List<Album> albums) {
        if (albums.isEmpty()) {
            System.out.println("No albums found.");
            return;
        }

        String border = "+" + "-".repeat(6) + "+" + "-".repeat(22) + "+" + "-".repeat(8) + "+" + "-".repeat(22) + "+";

        System.out.println(border);
        System.out.printf("| %-4s | %-20s | %-6s | %-20s |%n", "ID", "Name", "Year", "Artist");
        System.out.println(border);

        for (Album album : albums) {
            System.out.printf("| %-4d | %-20s | %-6d | %-20s |%n",
                    album.getId(), album.getName(), album.getYear(), album.getArtistName());
        }

        System.out.println(border);
    }

    private void denyAccess() {
        System.out.println("Access denied. Admins only.");
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