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

    public void addReservation(Reservation reservation) {
        confirmedBookings.add(reservation);
        System.out.println("Reservation " + reservation.getReservationId() + " added to booking history.");
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(confirmedBookings);
    }
}

class BookingReportService {
    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    public void printAllReservations() {
        List<Reservation> reservations = history.getAllReservations();
        if (reservations.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        System.out.println("Booking History:");
        System.out.println("ResID | Guest | Room | Nights | Total Cost");
        System.out.println("------------------------------------------");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    public void printSummary() {
        List<Reservation> reservations = history.getAllReservations();
        double totalRevenue = 0;
        for (Reservation r : reservations) {
            totalRevenue += r.getTotalCost();
        }
        System.out.println("\nTotal Bookings: " + reservations.size());
        System.out.println("Total Revenue: ₹" + totalRevenue);
    }
}

public class BookMyStay2 {

    public static void main(String[] args) {
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService(history);

        Reservation r1 = new Reservation("RES101", "Alice", "Deluxe", 3, 4500);
        Reservation r2 = new Reservation("RES102", "Bob", "Standard", 2, 3000);
        Reservation r3 = new Reservation("RES103", "Charlie", "Suite", 5, 12500);

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        System.out.println();
        reportService.printAllReservations();
        reportService.printSummary();
    }
}