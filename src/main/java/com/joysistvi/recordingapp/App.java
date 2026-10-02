package com.joysistvi.recordingapp;

import com.joysistvi.recordingapp.cliview.*;
import com.joysistvi.recordingapp.config.DbConnection;
import com.joysistvi.recordingapp.controller.AlbumController;
import com.joysistvi.recordingapp.controller.ArtistController;
import com.joysistvi.recordingapp.controller.PlaylistController;
import com.joysistvi.recordingapp.controller.SongController;
import com.joysistvi.recordingapp.controller.UserController;
import com.joysistvi.recordingapp.model.User;
import com.joysistvi.recordingapp.repository.AlbumRepository;
import com.joysistvi.recordingapp.repository.AlbumRepositoryImpl;
import com.joysistvi.recordingapp.repository.ArtistRepository;
import com.joysistvi.recordingapp.repository.ArtistRepositoryImpl;
import com.joysistvi.recordingapp.repository.PlaylistRepository;
import com.joysistvi.recordingapp.repository.PlaylistRepositoryImpl;
import com.joysistvi.recordingapp.repository.SongRepository;
import com.joysistvi.recordingapp.repository.SongRepositoryImpl;
import com.joysistvi.recordingapp.repository.UserRepository;
import com.joysistvi.recordingapp.repository.UserRepositoryImpl;
import com.joysistvi.recordingapp.service.AlbumService;
import com.joysistvi.recordingapp.service.AlbumServiceImpl;
import com.joysistvi.recordingapp.service.ArtistService;
import com.joysistvi.recordingapp.service.ArtistServiceImpl;
import com.joysistvi.recordingapp.service.PlaylistService;
import com.joysistvi.recordingapp.service.PlaylistServiceImpl;
import com.joysistvi.recordingapp.service.SongService;
import com.joysistvi.recordingapp.service.SongServiceImpl;
import com.joysistvi.recordingapp.service.UserService;
import com.joysistvi.recordingapp.service.UserServiceImpl;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DbConnection dbConnection = new DbConnection();

        // ----- Wiring: Repository -> Service -> Controller (role-agnostic, reused by both dashboards) -----
        SongRepository songRepository = new SongRepositoryImpl(dbConnection);
        SongService songService = new SongServiceImpl(songRepository);
        SongController songController = new SongController(songService);

        ArtistRepository artistRepository = new ArtistRepositoryImpl(dbConnection);
        ArtistService artistService = new ArtistServiceImpl(artistRepository);
        ArtistController artistController = new ArtistController(artistService);

        AlbumRepository albumRepository = new AlbumRepositoryImpl(dbConnection);
        AlbumService albumService = new AlbumServiceImpl(albumRepository);
        AlbumController albumController = new AlbumController(albumService);

        PlaylistRepository playlistRepository = new PlaylistRepositoryImpl(dbConnection);
        PlaylistService playlistService = new PlaylistServiceImpl(playlistRepository);
        PlaylistController playlistController = new PlaylistController(playlistService);

        UserRepository userRepository = new UserRepositoryImpl(dbConnection);
        UserService userService = new UserServiceImpl(userRepository);
        UserController userController = new UserController(userService);

        // ----- Welcome gate: must log in (or register) before entering the system -----
        boolean running = true;
        while (running) {
            printWelcomeMenu();
            int gateChoice = readInt(scanner);

            switch (gateChoice) {
                case 1 -> {
                    User loggedInUser = handleLogin(scanner, userController);
                    if (loggedInUser != null) {
                        if (loggedInUser.isAdmin()) {
                            runAdminDashboard(scanner, songController, albumController,
                                    artistController, userController);
                        } else {
                            runUserDashboard(scanner, songController, albumController,
                                    artistController, playlistController, loggedInUser.getId());
                        }
                    }
                }
                case 2 -> handleRegister(scanner, userController);
                case 0 -> {
                    System.out.println("Exiting Recording Studio App. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    private static void printWelcomeMenu() {
        clearScreen();
        System.out.println("\n--- RECORDING STUDIO APP ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private static User handleLogin(Scanner scanner, UserController userController) {
        clearScreen();
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userController.handleLogin(username, password);
        if (user != null) {
            System.out.println("Login successful! Welcome, " + user.getUsername()
                    + " (" + user.getRole() + ")");
        } else {
            System.out.println("Invalid username or password.");
        }
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
        return user;
    }

    private static void handleRegister(Scanner scanner, UserController userController) {
        clearScreen();
        System.out.println("\n--- REGISTER ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean success = userController.handleRegister(username, password);
        System.out.println(success
                ? "Registered successfully! You can now log in."
                : "Failed to register.");
        System.out.print("\nPress Enter to continue..."); // Opsyonal: Para hindi agad mawala ang mensahe
        scanner.nextLine();
    }

    // ----- Admin Dashboard: full CRUD over the music catalog + user management -----
    private static void runAdminDashboard(Scanner scanner, SongController songController,
                                          AlbumController albumController, ArtistController artistController,
                                          UserController userController) {
        SongView songView = new SongView(songController, scanner, true);
        AlbumView albumView = new AlbumView(albumController, scanner, true);
        ArtistView artistView = new ArtistView(artistController, scanner, true);
        UserView userView = new UserView(userController, scanner);

        int choice;
        do {
            printAdminMenu();
            choice = readInt(scanner);

            switch (choice) {
                case 1 -> songView.run();
                case 2 -> albumView.run();
                case 3 -> artistView.run();
                case 4 -> userView.run();
                case 0 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0);
    }

    private static void printAdminMenu() {
        clearScreen();
        System.out.println("\n===== ADMIN DASHBOARD =====");
        System.out.println("1. Song Management");
        System.out.println("2. Album Management");
        System.out.println("3. Artist Management");
        System.out.println("4. Manage Users");
        System.out.println("0. Logout");
        System.out.print("Choice: ");
    }

    // ----- User Dashboard: browse/search the catalog (read-only) + manage own playlists -----
    private static void runUserDashboard(Scanner scanner, SongController songController,
                                         AlbumController albumController, ArtistController artistController,
                                         PlaylistController playlistController, int userId) {
        SongView songView = new SongView(songController, scanner, false);
        AlbumView albumView = new AlbumView(albumController, scanner, false);
        ArtistView artistView = new ArtistView(artistController, scanner, false);
        PlaylistView playlistView = new PlaylistView(playlistController, scanner, userId);

        int choice;
        do {
            printUserMenu();
            choice = readInt(scanner);

            switch (choice) {
                case 1 -> songView.run();
                case 2 -> albumView.run();
                case 3 -> artistView.run();
                case 4 -> playlistView.run();
                case 0 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0);
    }

    private static void printUserMenu() {
        clearScreen();
        System.out.println("\n--- USER DASHBOARD ---");
        System.out.println("1. Browse Songs");
        System.out.println("2. Browse Albums");
        System.out.println("3. Browse Artists");
        System.out.println("4. My Playlists");
        System.out.println("0. Logout");
        System.out.print("Choice: ");
    }

    // Reads an int safely, re-prompting on invalid input, then consumes the trailing newline
    private static int readInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline
        return value;
    }

    private static void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // Fallback if the process can't be started
            System.out.println("\n".repeat(50));
        }
    }
}
