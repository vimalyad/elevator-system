package ElevatorSystem;

import ElevatorSystem.enums.Direction;
import ElevatorSystem.factories.ElevatorSystemFactory;
import ElevatorSystem.strategies.SCANStrategy;
import ElevatorSystem.system.ElevatorSystem;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=".repeat(80));
        System.out.println("  INITIALIZING SYSTEM (3 Elevators, Floors 0-15)");
        System.out.println("=".repeat(80));

        ElevatorSystem system = ElevatorSystemFactory.create(
                3, 0, 15, ElevatorSystemFactory.STRATEGY_NEAREST_CAR
        );


        System.out.println("\n" + "=".repeat(80));
        System.out.println("  SCENARIO 1: Concurrent Requests (NearestCar Strategy)");
        System.out.println("=".repeat(80));

        System.out.println("[MAIN] Firing multiple requests into the Global Queue...");
        system.requestElevator(3, Direction.UP);
        system.requestFloor(3, 7, Direction.UP);
        system.requestElevator(12, Direction.DOWN);

        System.out.println("[MAIN] Waiting 12 seconds for elevators to complete trips...\n");
        Thread.sleep(12000);


        System.out.println("\n" + "=".repeat(80));
        System.out.println("  SCENARIO 2: Hot-Swapping to SCAN Strategy & Mid-Transit Routing");
        System.out.println("=".repeat(80));

        System.out.println("[MAIN] Swapping Controller Brain to SCANStrategy...");
        system.setStrategy(new SCANStrategy());

        System.out.println("[MAIN] Requesting an elevator to go UP from 0 to 10...");
        system.requestFloor(0, 10, Direction.UP);

        Thread.sleep(3000);

        System.out.println("\n[MAIN] Mid-transit request: Passenger at Floor 8 wants to go UP.");
        System.out.println("[MAIN] SCAN should assign the elevator already moving UP past it.");
        system.requestElevator(8, Direction.UP);

        System.out.println("\n[MAIN] Waiting 15 seconds for elevators to resolve queue...\n");
        Thread.sleep(15000);


        System.out.println("\n" + "=".repeat(80));
        System.out.println("  SCENARIO 3: Emergency Alarm Interruption (Fatal)");
        System.out.println("=".repeat(80));

        System.out.println("[MAIN] Sending elevator on a long trip to Floor 15...");
        system.requestFloor(1, 15, Direction.UP);

        System.out.println("[MAIN] Waiting 4 seconds for elevator to gain speed...\n");
        Thread.sleep(4000);

        System.out.println("\n[MAIN] *** SOMEONE PRESSED THE ALARM BUTTON ***");
        system.triggerAlarm();

        System.out.println("[MAIN] Waiting 3 seconds to verify all movement has permanently stopped...\n");
        Thread.sleep(3000);


        System.out.println("\n" + "=".repeat(80));
        System.out.println("  ALL SCENARIOS COMPLETE. SHUTTING DOWN BACKGROUND THREADS.");
        System.out.println("=".repeat(80));

        system.shutdown();
    }
}