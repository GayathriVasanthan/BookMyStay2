import java.util.*;

class Service {
    private String serviceName;
    private double serviceCost;

    public Service(String serviceName, double serviceCost) {
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    public String toString() {
        return serviceName + " (₹" + serviceCost + ")";
    }
}

class AddOnServiceManager {
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    public void addService(String reservationId, Service service) {
        reservationServices.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
        System.out.println(service.getServiceName() + " added to reservation " + reservationId);
    }

    public void showServices(String reservationId) {
        List<Service> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services for reservation " + reservationId);
            return;
        }

        System.out.println("Services for reservation " + reservationId + ":");

        for (Service s : services) {
            System.out.println("- " + s);
        }
    }

    public double calculateTotalCost(String reservationId) {
        List<Service> services = reservationServices.get(reservationId);

        if (services == null) {
            return 0;
        }

        double total = 0;

        for (Service s : services) {
            total += s.getServiceCost();
        }

        return total;
    }
}

public class BookMyStay2 {

    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();

        String reservationId = "RES101";

        Service breakfast = new Service("Breakfast", 500);
        Service airportPickup = new Service("Airport Pickup", 1200);
        Service spa = new Service("Spa Access", 1500);

        manager.addService(reservationId, breakfast);
        manager.addService(reservationId, airportPickup);
        manager.addService(reservationId, spa);

        System.out.println();

        manager.showServices(reservationId);

        System.out.println();

        double totalCost = manager.calculateTotalCost(reservationId);

        System.out.println("Total Add-On Cost: ₹" + totalCost);
    }
}