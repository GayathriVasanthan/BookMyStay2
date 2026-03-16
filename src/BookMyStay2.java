import java.util.*;

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
    private List<Reservation> confirmedBookings = new ArrayList<>();
    private Stack<String> rollbackRoomStack = new Stack<>();
    private Map<String, Integer> inventory = new HashMap<>();

    public BookingHistory() {
        inventory.put("Standard", 5);
        inventory.put("Deluxe", 3);
        inventory.put("Suite", 2);
    }

    public boolean isCancellable(String reservationId) {
        for (Reservation r : confirmedBookings) {
            if (r.getReservationId().equals(reservationId)) {
                return true;
            }
        }
        return false;
    }

    public void addReservation(Reservation r) {
        confirmedBookings.add(r);
        decrementInventory(r.getRoomType());
        rollbackRoomStack.push(r.getRoomType());
        System.out.println("Reservation " + r.getReservationId() + " confirmed.");
    }

    public void cancelReservation(String reservationId) {
        if (!isCancellable(reservationId)) {
            System.out.println("Cancellation Error: Reservation " + reservationId + " does not exist.");
            return;
        }

        Reservation toCancel = null;
        for (Reservation r : confirmedBookings) {
            if (r.getReservationId().equals(reservationId)) {
                toCancel = r;
                break;
            }
        }

        confirmedBookings.remove(toCancel);
        rollbackInventory(toCancel.getRoomType());
        System.out.println("Reservation " + reservationId + " cancelled successfully.");
    }

    private void decrementInventory(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    private void rollbackInventory(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
        rollbackRoomStack.pop();
    }

    public void printInventory() {
        System.out.println("Current Inventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue() + " rooms available");
        }
    }

    public void printBookingHistory() {
        if (confirmedBookings.isEmpty()) {
            System.out.println("No active bookings.");
            return;
        }
        System.out.println("Active Bookings:");
        System.out.println("ResID | Guest | Room | Nights | Total Cost");
        System.out.println("------------------------------------------");
        for (Reservation r : confirmedBookings) {
            System.out.println(r);
        }
    }
}

public class BookMyStay2 {
    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();

        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe", 3, 4500);
        Reservation r2 = new Reservation("RES102", "Bob", "Standard", 2, 3000);
        Reservation r3 = new Reservation("RES103", "Charlie", "Suite", 5, 12500);

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        System.out.println();
        history.printBookingHistory();
        System.out.println();
        history.printInventory();

        System.out.println("\nAttempting cancellation of RES102...");
        history.cancelReservation("RES102");

        System.out.println();
        history.printBookingHistory();
        System.out.println();
        history.printInventory();

        System.out.println("\nAttempting cancellation of non-existent RES999...");
        history.cancelReservation("RES999");
    }
}