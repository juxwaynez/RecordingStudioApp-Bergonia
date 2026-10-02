package com.joysistvi.recordingapp.cliview;

import com.joysistvi.recordingapp.controller.SongController;
import com.joysistvi.recordingapp.model.Song;

import java.util.List;
import java.util.Scanner;

public class SongView {

    private final SongController songController; // Composition
    private final Scanner scanner;
    private final boolean isAdmin;

    // Constructor injection
    public SongView(SongController songController, Scanner scanner, boolean isAdmin) {
        this.songController = songController;
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
                case 1 -> viewAllSongs();
                case 2 -> searchSong();
                case 3 -> { if (isAdmin) addSong(); else denyAccess(); }
                case 4 -> { if (isAdmin) updateSong(); else denyAccess(); }
                case 5 -> { if (isAdmin) deleteSong(); else denyAccess(); }
                case 6 -> { if (isAdmin) archiveSong(); else denyAccess(); }
                case 7 -> { if (isAdmin) restoreSong(); else denyAccess(); }
                case 8 -> { if (isAdmin) viewArchivedSongs(); else denyAccess(); }
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
        clearScreen();
        System.out.println("\n---- " + (isAdmin ? "SONG MANAGEMENT" : "SONG CATALOG") + " ----");
        System.out.println("1. View All Songs");
        System.out.println("2. Search Song");
        if (isAdmin) {
            System.out.println("3. Add Song");
            System.out.println("4. Update Song");
            System.out.println("5. Delete Song");
            System.out.println("6. Archive Song");
            System.out.println("7. Restore Song");
            System.out.println("8. View Archived Songs");
        }
        System.out.println("0. Back");
    }

    private int promptChoice() {
        System.out.print("Choice: ");
        return readInt();
    }

    private void viewAllSongs() {
        System.out.println("\n----- View All Songs -----");
        List<Song> songs = songController.handleViewAllSongs();
        printSongs(songs);
    }

    private void viewArchivedSongs() {
        System.out.println("\n----- View All Archived Songs -----");
        List<Song> songs = songController.handleViewArchivedSongs();
        printSongs(songs);
    }

    private void searchSong() {
        System.out.println("\n----- Search Song -----");
        System.out.print("Enter title keyword: ");
        String keyword = scanner.nextLine();
        printSongs(songController.handleSearchSong(keyword));
    }

    private void addSong() {
        System.out.println("\n----- Add Song -----");
        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Length (e.g. 3:45): ");
        String length = scanner.nextLine();

        System.out.print("Genre: ");
        String genre = scanner.nextLine();

        System.out.print("Album ID: ");
        int albumId = readInt();

        Song song = new Song(title, length, genre, albumId);

        boolean success = songController.handleAddSong(song);
        System.out.println(success ? "Song added successfully." : "Failed to add song.");
    }

    private void updateSong() {
        System.out.println("\n----- Update Song -----");
        System.out.print("Song ID to update: ");
        int id = readInt();

        Song existingSong = songController.handleGetSongById(id);

        if (existingSong == null) {
            System.out.println("Song not found.");
            return;
        }

        System.out.println("\nPress Enter to keep the current value.");

        System.out.print("Title [" + existingSong.getTitle() + "]: ");
        String title = scanner.nextLine();
        if (title.isBlank()) {
            title = existingSong.getTitle();
        }

        System.out.print("Length [" + existingSong.getLength() + "]: ");
        String length = scanner.nextLine();
        if (length.isBlank()) {
            length = existingSong.getLength();
        }

        System.out.print("Genre [" + existingSong.getGenre() + "]: ");
        String genre = scanner.nextLine();
        if (genre.isBlank()) {
            genre = existingSong.getGenre();
        }

        System.out.print("Album ID [" + existingSong.getAlbumId() + "]: ");
        String albumInput = scanner.nextLine();

        int albumId = existingSong.getAlbumId();

        if (!albumInput.isBlank()) {
            try {
                albumId = Integer.parseInt(albumInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid Album ID. Keeping the current value.");
            }
        }

        Song updatedSong = new Song(id, title, length, genre, albumId);

        boolean success = songController.handleUpdateSong(updatedSong);

        System.out.println(success
                ? "Song updated successfully."
                : "Failed to update song.");
    }

    private void deleteSong() {
        System.out.println("\n----- Delete Song -----");
        System.out.print("Song ID to delete: ");
        int id = readInt();

        boolean success = songController.handleDeleteSong(id);
        System.out.println(success ? "Song deleted successfully." : "Failed to delete song.");
    }

    private void archiveSong() {
        System.out.println("\n----- Archive Song -----");
        System.out.print("Song ID to archive: ");
        int id = readInt();

        boolean success = songController.handleArchiveSong(id);
        System.out.println(success ? "Song archived successfully." : "Failed to archive song.");
    }

    private void restoreSong() {
        System.out.println("\n----- Restore Song -----");
        System.out.print("Song ID to restore: ");
        int id = readInt();

        boolean success = songController.handleRestoreSong(id);
        System.out.println(success ? "Song restored successfully." : "Failed to restore song.");
    }

    private void printSongs(List<Song> songs) {

        if (songs.isEmpty()) {
            System.out.println("No songs found.");
            return;
        }

        String border = "+" + "-".repeat(6) + "+" + "-".repeat(27) + "+"
                + "-".repeat(10) + "+" + "-".repeat(14) + "+" + "-".repeat(17) + "+";

        System.out.println(border);
        System.out.printf("| %-4s | %-25s | %-8s | %-12s | %-15s |%n",
                "ID", "Title", "Length", "Genre", "Album");
        System.out.println(border);

        for (Song song : songs) {
            System.out.printf("| %-4d | %-25s | %-8s | %-12s | %-15s |%n",
                    song.getId(), song.getTitle(), song.getLength(), song.getGenre(), song.getAlbumName());
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
