import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;
    private double totalCost;

    public Reservation(String reservationId, String guestName, String roomType, int nights, double totalCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.totalCost = totalCost;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public int getNights() { return nights; }
    public double getTotalCost() { return totalCost; }

    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType + " | " + nights + " nights | ₹" + totalCost;
    }
}

class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Reservation> confirmedBookings = new ArrayList<>();
    private Map<String, Integer> inventory = new HashMap<>();

    public BookingHistory() {
        inventory.put("Standard", 5);
        inventory.put("Deluxe", 3);
        inventory.put("Suite", 2);
    }

    public boolean addReservation(Reservation r) {
        int available = inventory.getOrDefault(r.getRoomType(), 0);
        if (available <= 0) {
            System.out.println("Booking Failed for " + r.getGuestName() + ": No " + r.getRoomType() + " rooms available");
            return false;
        }
        inventory.put(r.getRoomType(), available - 1);
        confirmedBookings.add(r);
        System.out.println("Reservation confirmed: " + r.getReservationId() + " for " + r.getGuestName());
        return true;
    }

    public void printBookingHistory() {
        if (confirmedBookings.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }
        System.out.println("Booking History:");
        System.out.println("ResID | Guest | Room | Nights | Total Cost");
        System.out.println("------------------------------------------");
        for (Reservation r : confirmedBookings) {
            System.out.println(r);
        }
    }

    public void printInventory() {
        System.out.println("Current Inventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue() + " rooms available");
        }
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
            System.out.println("Booking history saved successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving booking history: " + e.getMessage());
        }
    }

    public static BookingHistory loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (BookingHistory) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Persistence file not found. Starting with new state.");
            return new BookingHistory();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading booking history. Starting with new state.");
            return new BookingHistory();
        }
    }
}

public class BookMyStay2 {
    public static void main(String[] args) {
        String persistenceFile = "booking_history.dat";

        BookingHistory history = BookingHistory.loadFromFile(persistenceFile);

        // Example bookings
        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe", 3, 4500);
        Reservation r2 = new Reservation("RES102", "Bob", "Standard", 2, 3000);

        history.addReservation(r1);
        history.addReservation(r2);

        System.out.println();
        history.printBookingHistory();
        System.out.println();
        history.printInventory();

        // Save state for next run
        history.saveToFile(persistenceFile);
    }
}
