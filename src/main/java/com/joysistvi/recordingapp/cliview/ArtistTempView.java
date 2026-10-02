package com.joysistvi.recordingapp.cliview;

import com.joysistvi.recordingapp.controller.ArtistController;
import com.joysistvi.recordingapp.model.Artist;

import java.util.List;
import java.util.Scanner;

public class ArtistTempView {

    private final ArtistController artistController; // Composition
    private final Scanner scanner;

    // Constructor injection
    public ArtistTempView(ArtistController artistController, Scanner scanner) {
        this.artistController = artistController;
        this.scanner = scanner;
    }

    public void run() {
        int choice;
        do {
            clearScreen();
            printMenu();
            choice = promptChoice();

            switch (choice) {
                case 1 -> viewAllArtists();
                case 2 -> searchArtist();
                case 3 -> addArtist();
                case 4 -> updateArtist();
                case 5 -> deleteArtist();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice. Try again.");
            }

            if (choice != 0) {
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
            }
        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println("\n===== ARTIST MANAGEMENT =====");
        System.out.println("1. View All Artists");
        System.out.println("2. Search Artist");
        System.out.println("3. Add Artist");
        System.out.println("4. Update Artist");
        System.out.println("5. Delete Artist");
        System.out.println("0. Back");
    }

    private int promptChoice() {
        System.out.print("Choice: ");
        return readInt();
    }

    private void viewAllArtists() {
        System.out.println("\n----- Artist Table -----");
        List<Artist> artists = artistController.handleViewAllArtists();
        printArtists(artists);
    }

    private void searchArtist() {
        System.out.println("\n----- Search Artist -----");
        System.out.print("Enter name keyword: ");
        String keyword = scanner.nextLine();
        printArtists(artistController.handleSearchArtist(keyword));
    }

    private void addArtist() {
        System.out.println("\n----- Add Artist -----");
        System.out.print("Name: ");
        String name = scanner.nextLine();

        Artist artist = new Artist(name);

        boolean success = artistController.handleAddArtist(artist);
        System.out.println(success ? "Artist added successfully." : "Failed to add artist.");

        if (success) {
            System.out.println();
            viewAllArtists(); // refresh-after-mutation: show the current state, not just a message
        }
    }

    private void updateArtist() {
        System.out.println("\n----- Update Artist -----");

        // Show all artists first so the admin can see which ID to pick
        viewAllArtists();

        System.out.print("Artist ID to update: ");
        int id = readInt();

        // Get the current name so we can keep it if the admin just presses Enter
        Artist current = artistController.handleGetArtistById(id);
        if (current == null) {
            System.out.println("No artist found with ID " + id + ". Please check the ID and try again.");
            return;
        }

        System.out.print("New Name [" + current.getName() + "] (press Enter to keep current): ");
        String name = scanner.nextLine();
        if (name.trim().isEmpty()) {
            name = current.getName();
        }

        Artist artist = new Artist(id, name);

        boolean success = artistController.handleUpdateArtist(artist);
        System.out.println(success ? "Artist updated successfully." : "Failed to update artist.");

        if (success) {
            System.out.println();
            viewAllArtists(); // refresh-after-mutation
        }
    }

    private void deleteArtist() {
        System.out.println("\n----- Delete Artist -----");
        System.out.print("Artist ID to delete: ");
        int id = readInt();

        // Check existence first so we can give a specific, actionable message
        Artist artist = artistController.handleGetArtistById(id);
        if (artist == null) {
            System.out.println("No artist found with ID " + id + ". Please check the ID and try again.");
            return;
        }

        System.out.println("You are about to delete: " + artist.getName());
        System.out.print("Confirm delete? (Y/N): ");
        String confirm = scanner.nextLine();
        if (!confirm.trim().equalsIgnoreCase("Y")) {
            System.out.println("Delete cancelled.");
            return;
        }

        boolean success = artistController.handleDeleteArtist(id);
        System.out.println(success
                ? "Artist deleted successfully."
                : "Failed to delete artist due to an unexpected error. Please try again.");

        if (success) {
            System.out.println();
            viewAllArtists(); // refresh-after-mutation
        }
    }

    private void printArtists(List<Artist> artists) {
        if (artists.isEmpty()) {
            System.out.println("No artists found.");
            return;
        }

        String border = "+" + "-".repeat(6) + "+" + "-".repeat(27) + "+";

        System.out.println(border);
        System.out.printf("| %-4s | %-25s |%n", "ID", "Name");
        System.out.println(border);

        for (Artist artist : artists) {
            System.out.printf("| %-4d | %-25s |%n", artist.getId(), artist.getName());
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
        while (true) {
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

}
