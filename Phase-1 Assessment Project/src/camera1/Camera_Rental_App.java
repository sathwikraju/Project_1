package camera1;


import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Camera_Rental_App {
    private static List<Camera> cameraList = new ArrayList<>();
	private static double walletBalance = 0;
    private static boolean loggedIn = false;
    private static String loggedInUser = "";
    private static String loggedInUserRole = "";

    public static void main(String[] args) {
        displayWelcomeScreen();
        login();
	    }

    private static void displayWelcomeScreen() {
    	System.out.println("+---------------------------+");
        System.out.println("|  Welcome to rentmycam.io  |");
        System.out.println("+---------------------------+");
        System.out.println("Developed by Sathwik Raju \n");
        
        }

	private static void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("PLEASE LOGIN TO CONTINUE");
        while (!loggedIn) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (authenticateUser(username, password)) {
                System.out.println("Login successful!");
                loggedIn = true;
                break;
                } 
            else {
                	System.out.println("Invalid username or password. Please try again.");
                	}
            }
        if (loggedIn) {
	        System.out.println("Logged in as: " + loggedInUser);
	        if (loggedInUserRole.equals("admin")) {
                showAdminMainMenu();
            } else {
                showUserMainMenu();
            }
        }
    }

    private static boolean authenticateUser(String username, String password) {
        // Simulating user authentication
        if (username.equals("admin") && password.equals("pass")) {
            loggedInUser = "Raju";
            loggedInUserRole = "admin";
            return true;
	        } else if (username.equals("user") && password.equals("password")) {
	            loggedInUser = "user";
	            loggedInUserRole = "user";
	            return true;
	        }
	        return false;
	    }

	    private static void showAdminMainMenu() {
	        while (true) {
	            System.out.println("\n---- Admin Main Menu ----");
	            System.out.println("1. Add a Camera");
	            System.out.println("2. Remove a Camera");
	            System.out.println("3. View Available Cameras");
	            System.out.println("4. Logout");
	            System.out.println("5. Close the Application");

	            int choice = getUserChoice(5);
	            switch (choice) {
	                case 1:
	                    addCamera();
	                    break;
	                case 2:
	                    removeCamera();
	                    break;
	                case 3:
	                    displayAvailableCameras();
	                    break;
	                case 4:
	                    logout();
	                    break;
	                case 5:
	                	closeApplication();
	                	break;
	            }
	        }
	    }

	    private static void showUserMainMenu() {
	        while (true) {
	            System.out.println("\n---- User Main Menu ----");
	            System.out.println("1. View Available Cameras");
	            System.out.println("2. Rent a Camera");
	            System.out.println("3. Manage Wallet Balance");
	            System.out.println("4. Logout");
	            System.out.println("5. Close the Application");

	            int choice = getUserChoice(5);
	            switch (choice) {
	                case 1:
	                    displayAvailableCameras();
	                    break;
	                case 2:
	                    rentCamera();
	                    break;
	                case 3:
	                    manageWalletBalance();
	                    break;
	                case 4:
	                    logout();
	                    break;
	                case 5:
	                	closeApplication();
	                	break;
	            }
	        }
	    }

	    private static void addCamera() {
	        if (!loggedInUserRole.equals("admin")) {
	            System.out.println("You are not authorized to perform this action.");
	            return;
	        }

	        System.out.println("\n---- Add a Camera ----");
	        Scanner scanner = new Scanner(System.in);

	        System.out.print("Enter camera brand: ");
	        String brand = scanner.nextLine();

	        System.out.print("Enter camera model: ");
	        String model = scanner.nextLine();

	        double rentalAmount = getPositiveDoubleInput("Enter per-day rental amount: ");
	        cameraList.add(new Camera(brand, model, rentalAmount, true));
	        System.out.println("Camera listed successfully!");
	    }

	    private static void removeCamera() {
	        if (!loggedInUserRole.equals("admin")) {
	            System.out.println("You are not authorized to perform this action.");
	            return;
	        }

	        System.out.println("\n---- Remove a Camera ----");
	        if (cameraList.isEmpty()) {
	            System.out.println("No cameras available.");
	            return;
	        }

	        displayAvailableCameras();
	        int cameraIndex = getUserChoice(cameraList.size()) - 1;
	        cameraList.remove(cameraIndex);
	        System.out.println("Camera removed successfully!");
	    }

	    private static void displayAvailableCameras() {
	        System.out.println("\n---- Available Cameras ----");
	        if (cameraList.isEmpty()) {
	            System.out.println("No cameras available.");
	        } else {
	        	System.out.println("===================================================================");
	            System.out.println(String.format("%-5s %-15s %-15s %-15s %-15s", "ID", "Brand", "Model", "Price per Day", "Availability"));
	            System.out.println("===================================================================");
	            for (int i = 0; i < cameraList.size(); i++) {
	                Camera camera = cameraList.get(i);
	                String availability = camera.isAvailable() ? "Available" : "Rented";
	                System.out.println(String.format("%-5s %-15s %-15s %-15s %-15s", (i + 1), camera.getBrand(), camera.getModel(), camera.getRentalAmount(), availability));
	            }
	        }
	    }


	    private static void rentCamera() {
	        System.out.println("\n---- Rent a Camera ----");
	        if (cameraList.isEmpty()) {
	            System.out.println("No cameras available for rent.");
	            return;
	        }

	        displayAvailableCameras();
	        int cameraIndex = getUserChoice(cameraList.size()) - 1;
	        Camera selectedCamera = cameraList.get(cameraIndex);

	        if (!selectedCamera.isAvailable()) {
	            System.out.println("Camera is currently rented.");
	        } else if (selectedCamera.getRentalAmount() > walletBalance) {
	            System.out.println("Insufficient wallet amount. Please deposit more funds.");
	        } else {
	            selectedCamera.setAvailable(false);
	            walletBalance -= selectedCamera.getRentalAmount();
	            System.out.println("Camera rented successfully!");
	        }
	    }

	    private static void manageWalletBalance() {
	        while (true) {
	            System.out.println("\n---- Wallet Management ----");
	            System.out.println("1. Add Amount");
	            System.out.println("2. View Amount");
	            System.out.println("3. Return to Main Menu");

	            int choice = getUserChoice(3);
	            switch (choice) {
	                case 1:
	                    addWalletAmount();
	                    break;
	                case 2:
	                    viewWalletAmount();
	                    break;
	                case 3:
	                    return;
	            }
	        }
	    }

	    private static void addWalletAmount() {
	        System.out.println("\n---- Add Amount ----");
	        double amount = getPositiveDoubleInput("Enter the amount to deposit: ");
	        walletBalance += amount;
	        System.out.println("Amount deposited successfully!");
	    }

	    private static void viewWalletAmount() {
	        System.out.println("\n---- View Amount ----");
	        System.out.println("Wallet Balance: " + walletBalance);
	    }

	    private static void closeApplication() {
	        System.out.println("\nClosing the application. Thank you!");
	        System.exit(0);
	    }
	    
	    private static void logout() {
	        loggedIn = false;
	        loggedInUser = "";
	        loggedInUserRole = "";
	        System.out.println("\nLogged out successfully.");
	        login();
	    }

	    private static int getUserChoice(int maxChoice) {
	        Scanner scanner = new Scanner(System.in);
	        while (true) {
	            System.out.print("Enter your choice: ");
	            try {
	                int choice = scanner.nextInt();
	                if (choice >= 1 && choice <= maxChoice) {
	                    return choice;
	                } else {
	                    System.out.println("Invalid choice! Please try again.");
	                }
	            } catch (InputMismatchException e) {
	                System.out.println("Invalid input! Please enter a valid choice.");
	                scanner.nextLine(); // Clear the input buffer
	            }
	        }
	    }

	    private static double getPositiveDoubleInput(String message) {
	        Scanner scanner = new Scanner(System.in);
	        while (true) {
	            System.out.print(message);
	            try {
	                double input = scanner.nextDouble();
	                if (input > 0) {
	                    return input;
	                } else {
	                    System.out.println("Invalid input! Please enter a positive value.");
	                }
	            } catch (InputMismatchException e) {
	                System.out.println("Invalid input! Please enter a valid number.");
	                scanner.nextLine(); // Clear the input buffer
	            }
	        }
	    }
	}

	class Camera {
	    private String brand;
	    private String model;
	    private double rentalAmount;
	    private boolean available;

	    public Camera(String brand, String model, double rentalAmount, boolean available) {
	        this.brand = brand;
	        this.model = model;
	        this.rentalAmount = rentalAmount;
	        this.available = available;
	    }

	    public String getBrand() {
	        return brand;
	    }

	    public String getModel() {
	        return model;
	    }

	    public double getRentalAmount() {
	        return rentalAmount;
	    }

	    public boolean isAvailable() {
	        return available;
	    }

	    public void setAvailable(boolean available) {
	        this.available = available;
	    }

	    @Override
	    public String toString() {
	        String status = available ? "Available" : "Rented";
	        return "Camera: " + brand + " " + model + " | Rental Amount: " + rentalAmount + " | Status: " + status;
	    }
	}
