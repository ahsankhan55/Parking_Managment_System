import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

abstract class AbstractVehicle {
    private String licensePlate;
    private String category;

    public AbstractVehicle(String licensePlate, String category) {
        this.licensePlate = licensePlate;
        this.category = category;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getCategory() {
        return category;
    }

    public abstract void displayAdditionalInfo();
}

class Vehicle extends AbstractVehicle {
    public Vehicle(String licensePlate, String category) {
        super(licensePlate, category);
    }

    @Override
    public void displayAdditionalInfo() {
        System.out.println("This is a regular vehicle.");
        //return "This is a regular vehicle.";
    }
}

class Car extends AbstractVehicle {
    private int numberOfDoors;

    public Car(String licensePlate, String category, int numberOfDoors) {
        super(licensePlate, category);
        this.numberOfDoors = numberOfDoors;
    }

    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    @Override
    public void displayAdditionalInfo() {
        System.out.println("This is a car with " + numberOfDoors + " doors.");
        //return "This is a car with " + numberOfDoors + " doors.";
    }
}

class Motorcycle extends AbstractVehicle {
    private boolean hasSidecar;

    public Motorcycle(String licensePlate, String category, boolean hasSidecar) {
        super(licensePlate, category);
        this.hasSidecar = hasSidecar;
    }

    public boolean hasSidecar() {
        return hasSidecar;
    }

    @Override
    public void displayAdditionalInfo() {
        if (hasSidecar) {
            System.out.println("This is a motorcycle with a sidecar.");
            //return "This is a motorcycle with a sidecar.";
        } else {
            System.out.println("This is a regular motorcycle.");
            //return "This is a regular motorcycle.";
        }
    }
}

class ParkingSpot {
    private int spotNumber;
    private AbstractVehicle vehicle;
    private LocalDateTime parkedTime;

    public ParkingSpot(int spotNumber) {
        this.spotNumber = spotNumber;
        this.vehicle = null;
        this.parkedTime = null;
    }

    public void parkVehicle(AbstractVehicle vehicle) {
        UI a = new UI();
        if (isSpotEmpty()) {
            this.vehicle = vehicle;
            this.parkedTime = LocalDateTime.now();
           a.outputs("Vehicle " + vehicle.getLicensePlate() + " parked at spot " + spotNumber + ".");
         //   return "Vehicle " + vehicle.getLicensePlate() + " parked at spot " + spotNumber + ".";
        } else {
          a.outputs("Spot " + spotNumber + " is already occupied.");
          //  return "Spot " + spotNumber + " is already occupied.";
        }
    }

    public void removeVehicle() {
        UI a = new UI();
        if (!isSpotEmpty()) {
            AbstractVehicle removedVehicle = this.vehicle;
            this.vehicle = null;
            this.parkedTime = null;

            a.outputs("Vehicle " + removedVehicle.getLicensePlate() + " removed from spot " + spotNumber + ".");
        } else {
            a.outputs("Spot " + spotNumber + " is already empty.");
        }
    }

    public boolean isSpotEmpty() {
        return vehicle == null;
    }

    public LocalDateTime getParkedTime() {
        return parkedTime;
    }

    public Integer getSpotNumber() {
        return spotNumber;
    }
}

class ParkingLot {
    private int capacity;
    private List<ParkingSpot> spots;
    private Map<String, ParkingSpot> parkedVehicles;
    private Map<String, List<String>> vehicleCategories;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        this.spots = new ArrayList<>(capacity);
        this.parkedVehicles = new HashMap<>();
        this.vehicleCategories = new HashMap<>();
        for (int i = 0; i < capacity; i++) {
            spots.add(new ParkingSpot(i));
        }
    }

    public void parkVehicle(AbstractVehicle vehicle) {
        if (isVehicleParked(vehicle)) {
            System.out.println("Vehicle " + vehicle.getLicensePlate() + " is already parked.");
            //return "Vehicle " + vehicle.getLicensePlate() + " is already parked.";
        }

        List<String> categoryVehicles = vehicleCategories.getOrDefault(vehicle.getCategory(), new ArrayList<>());
        if (categoryVehicles.size() < capacity) {
            for (ParkingSpot spot : spots) {
                if (spot.isSpotEmpty()) {
                    spot.parkVehicle(vehicle);
                    parkedVehicles.put(vehicle.getLicensePlate(), spot);
                    categoryVehicles.add(vehicle.getLicensePlate());
                    vehicleCategories.put(vehicle.getCategory(), categoryVehicles);
                    return;
                }
            }
        } else {
            System.out.println("No available spots for category " + vehicle.getCategory() + ".");
            //return "No available spots for category " + vehicle.getCategory() + ".";
        }
        System.out.println("No available spots.");
        //return "No available spots.";
    }

    public void removeVehicle(String licensePlate) {
        if (parkedVehicles.containsKey(licensePlate)) {
            ParkingSpot spot = parkedVehicles.get(licensePlate);
            spot.removeVehicle();
            parkedVehicles.remove(licensePlate);

            AbstractVehicle removedVehicle = null;
            for (String category : vehicleCategories.keySet()) {
                List<String> categoryVehicles = vehicleCategories.get(category);
                if (categoryVehicles.contains(licensePlate)) {
                    categoryVehicles.remove(licensePlate);
                    if (removedVehicle == null) {
                        removedVehicle = new Vehicle(licensePlate, category);
                    }
                }
            }
            if (removedVehicle != null) {
                List<String> categoryVehicles = vehicleCategories.get(removedVehicle.getCategory());
                if (categoryVehicles.isEmpty()) {
                    vehicleCategories.remove(removedVehicle.getCategory());
                }
            }
        } else {
            System.out.println("Vehicle " + licensePlate + " is not present in the parking lot.");
        }
    }

    public boolean isVehicleParked(AbstractVehicle vehicle) {
        return parkedVehicles.containsKey(vehicle.getLicensePlate());
    }

    public List<Integer> getAvailableSpots() {
        List<Integer> availableSpots = new ArrayList<>();
        for (ParkingSpot spot : spots) {
            if (spot.isSpotEmpty()) {
                availableSpots.add(spot.getSpotNumber());
            }
        }
        return availableSpots;
    }

    public double calculateParkingPrice(String licensePlate) {
        if (parkedVehicles.containsKey(licensePlate)) {
            ParkingSpot spot = parkedVehicles.get(licensePlate);
            LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime parkedTime = spot.getParkedTime();
            Duration duration = Duration.between(parkedTime, currentTime);
            long hours = duration.toMinutes();
            return hours * 5.0;
        }
        return 0.0;
    }
}

public class Main {
    public static void main(String[] args) {
        UI a = new UI();
        Scanner scanner = new Scanner(System.in);
        ParkingLot parkingLot = new ParkingLot(5);
        boolean running = true;

        while (running) {

            System.out.println("Choose an option:");
            System.out.println("1. Park Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. Check Available Spots");
            System.out.println("4. Calculate Parking Price");
            System.out.println("5. Exit");

            int option = scanner.nextInt();

            scanner.nextLine(); // Consume newline character5

            switch (option) {
                case 1:
                    System.out.print("Enter license plate: ");
                    String licensePlate = scanner.nextLine();
                    System.out.print("Enter vehicle category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter additional information: ");
                    String additionalInfo = scanner.nextLine();

                    AbstractVehicle vehicle;
                    if (category.equalsIgnoreCase("car")) {
                        System.out.print("Enter number of doors: ");
                        int numberOfDoors = scanner.nextInt();
                        vehicle = new Car(licensePlate, category, numberOfDoors);
                    } else if (category.equalsIgnoreCase("motorcycle")) {
                        System.out.print("Does the motorcycle have a sidecar? (true/false): ");
                        boolean hasSidecar = scanner.nextBoolean();
                        vehicle = new Motorcycle(licensePlate, category, hasSidecar);
                    } else {
                        vehicle = new Vehicle(licensePlate, category);
                    }

                    vehicle.displayAdditionalInfo();    //vehicle.displayAdditionalInfo();
                    parkingLot.parkVehicle(vehicle);
                    break;
                case 2:
                    System.out.print("Enter license plate: ");
                    licensePlate = scanner.nextLine();
                    parkingLot.removeVehicle(licensePlate);
                    break;
                case 3:
                    List<Integer> availableSpots = parkingLot.getAvailableSpots();

                    String ab=("Available spots: " + availableSpots);
                    a.outputs(ab);
                    break;
                case 4:
                    System.out.print("Enter license plate: ");
                    licensePlate = scanner.nextLine();
                    double parkingPrice = parkingLot.calculateParkingPrice(licensePlate);
                    String abc=("Parking price for " + licensePlate + ": $" + parkingPrice);
                    a.outputs(abc);
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    a.outputs("Invalid option.");
                    break;
            }

            System.out.println();
        }

        scanner.close();
    }
}