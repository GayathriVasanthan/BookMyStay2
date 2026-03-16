import java.util.*;
import java.util.concurrent.*;

class Reservation {
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

class BookingHistory {
    private List<Reservation> confirmedBookings = Collections.synchronizedList(new ArrayList<>());
    private Map<String, Integer> inventory = new ConcurrentHashMap<>();

    public BookingHistory() {
        inventory.put("Standard", 5);
        inventory.put("Deluxe", 3);
        inventory.put("Suite", 2);
    }

    public synchronized boolean addReservation(Reservation r) {
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

    public void printInventory() {
        System.out.println("Current Inventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue() + " rooms available");
        }
    }

    public void printBookingHistory() {
        synchronized (confirmedBookings) {
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
    }
}

class GuestBookingThread extends Thread {
    private BookingHistory history;
    private Reservation reservation;

    public GuestBookingThread(BookingHistory history, Reservation reservation) {
        this.history = history;
        this.reservation = reservation;
    }

    @Override
    public void run() {
        history.addReservation(reservation);
    }
}

public class BookMyStay2 {
    public static void main(String[] args) throws InterruptedException {

        BookingHistory history = new BookingHistory();

        List<GuestBookingThread> threads = new ArrayList<>();

        threads.add(new GuestBookingThread(history, new Reservation("RES101", "Alice", "Deluxe", 3, 4500)));
        threads.add(new GuestBookingThread(history, new Reservation("RES102", "Bob", "Standard", 2, 3000)));
        threads.add(new GuestBookingThread(history, new Reservation("RES103", "Charlie", "Suite", 5, 12500)));
        threads.add(new GuestBookingThread(history, new Reservation("RES104", "David", "Suite", 1, 5000)));
        threads.add(new GuestBookingThread(history, new Reservation("RES105", "Eve", "Deluxe", 2, 4000)));
        threads.add(new GuestBookingThread(history, new Reservation("RES106", "Frank", "Standard", 3, 4500)));
        threads.add(new GuestBookingThread(history, new Reservation("RES107", "Grace", "Deluxe", 1, 2000)));

        for (GuestBookingThread t : threads) {
            t.start();
        }

        for (GuestBookingThread t : threads) {
            t.join();
        }

        System.out.println();
        history.printBookingHistory();
        System.out.println();
        history.printInventory();
    }
}