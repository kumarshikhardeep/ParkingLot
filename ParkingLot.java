import java.io.*;
import java.util.*;

public class ParkingLot{

    // Commands to be executed
    private static final String Create_parking_lot = "Create_parking_lot";
    private static final String Park = "Park";
    private static final String Slot_numbers_for_driver_of_age = "Slot_numbers_for_driver_of_age";
    private static final String Slot_number_for_car_with_number = "Slot_number_for_car_with_number";
    private static final String Leave = "Leave";
    private static final String Vehicle_registration_number_for_driver_of_age = "Vehicle_registration_number_for_driver_of_age";

    private PriorityQueue<Integer> parkingLot = new PriorityQueue<Integer>(); // min heap to store the available slots
    private Map<Integer, String> slotToReg = new HashMap<Integer, String>();
    private Map<Integer, ArrayList<String>> ageToReg = new HashMap<Integer, ArrayList<String>>();
    private Map<String, Integer> regToAge = new HashMap<String, Integer>();
    private Map<Integer, ArrayList<Integer>> ageToSlots = new HashMap<Integer, ArrayList<Integer>>();
    private Map<String, Integer> regToSlot = new HashMap<String, Integer>();
    private int maxSlot = 1; //

    /**
     * create a parking lot of given size. Any time a new parking lot is created,
     * the size of existing parking lot will increase by the given size
     * @param numOfSlots
     */
    private void createParking(int numOfSlots){
        for(int slot=1; slot<=numOfSlots; slot++){
            parkingLot.add(maxSlot++);
        }
        System.out.println("Created parking of " + numOfSlots + " slots");
    }

    /**
     * Park a vehicle. The parking slot allotted will be the nearest to the entry (top element of min-heap @parkingLot).
     * If all slots are occupied, it says "No slots available"
     * The top element will be deleted, indicating that particular slot is filled.
     * The slot number, registration number and driver's age will be stored.
     * @param regNum
     * @param driverAge
     */
    private void parkVehicle(String regNum, int driverAge){
        if(parkingLot.size()==0){
            System.out.println("No slots available");
            return;
        }

        if(!ageToReg.containsKey(driverAge)){
            ageToReg.put(driverAge, new ArrayList<>());
        }
        ArrayList<String> regTemp = ageToReg.get(driverAge);
        regTemp.add(regNum);
        ageToReg.put(driverAge, regTemp);

        regToAge.put(regNum, driverAge);

        int availableSlot = parkingLot.peek();
        slotToReg.put(availableSlot, regNum);

        if(!ageToSlots.containsKey(driverAge)){
            ageToSlots.put(driverAge, new ArrayList<Integer>());
        }
        ArrayList<Integer> slotTemp = ageToSlots.get(driverAge);
        slotTemp.add(availableSlot);
        ageToSlots.put(driverAge, slotTemp);

        regToSlot.put(regNum, availableSlot);

        System.out.println("Car with vehicle registration number “" + regNum + "” has been parked at slot number " + availableSlot);
        parkingLot.poll();
    }

    /**
     * Given the driver's age, get all the slot numbers where the vehicles
     * of that particular age are parked. Print "null" if no result is found.
     * @param driverAge
     */
    private void getSlotNumbersOfDriversOfAge(int driverAge){
        if(!ageToSlots.containsKey(driverAge)){
            System.out.println("null");
        }

        ArrayList<Integer> slots = ageToSlots.get(driverAge);
        for(int i=0; i<slots.size()-1; i++)
            System.out.print(slots.get(i)+",");
        System.out.print(slots.get(slots.size()-1) + "\n");
    }

    /**
     * Given the registration number, get the slot number where this vehicle is parked.
     * Print "null" if no result is found.
     * @param regNum
     */
    private void getSlotNumberOfCarWithNumber(String regNum){
        if(!regToSlot.containsKey(regNum))
            System.out.println("null");
        System.out.println(regToSlot.get(regNum));
    }

    /**
     * If a given slot becomes available again,
     * update the @parkingLot by adding this slot,
     * delete the stored information corresponding to that slot i.e., registration number, driver's age.
     * It also handles the cases if the given slot is already available or the slot number is out of range.
     * @param slotNum
     */
    private void leaveParking(int slotNum){
        if(parkingLot.contains(slotNum)){
            System.out.println("Slot " + slotNum + " is already vacant");
            return;
        }
        else if(slotNum >= this.maxSlot){
            System.out.println("Slot " + slotNum + " is out of range");
            return;
        }
        this.parkingLot.add(slotNum);

        String regNum = slotToReg.get(slotNum);
        int age = regToAge.get(regNum);

        this.slotToReg.remove(slotNum);
        if(ageToReg.containsKey(age)){
            ageToReg.get(age).remove(regNum);
        }
        this.regToAge.remove(regNum);
        if(ageToSlots.containsKey(age)){
            ageToSlots.get(age).remove((Object)slotNum);
        }
        this.regToSlot.remove(regNum);
        System.out.println("Slot number " + slotNum + " vacated, the car with vehicle registration number “" + regNum + "” left the space, the driver of the car was of age " + age);
    }

    /**
     * Given the driver's age, get all the registration numbers of the vehicles
     * currently parked. Print "null" if no result is found.
     * @param driverAge
     */
    private void getRegNumbersOfDriverAge(int driverAge){
        if(!ageToReg.containsKey(driverAge)) {
            System.out.println("null");
            return;
        }
        ArrayList<String> regNums = this.ageToReg.get(driverAge);
        for(int i=0; i<regNums.size()-1; i++){
            System.out.print(regNums.get(i)+",");
        }
        System.out.print(regNums.get(regNums.size()-1)+"\n");
    }

    private void executeCommand(String[] command){
        int driverAge, slotNum;
        String regNum;
        switch (command[0]) {

            case Create_parking_lot:
                int numOfSlots = Integer.parseInt(command[1]);
                createParking(numOfSlots);
                break;

            case Park:
                regNum = command[1];
                driverAge = Integer.parseInt(command[3]);
                parkVehicle(regNum, driverAge);
                break;

            case Slot_numbers_for_driver_of_age:
                driverAge = Integer.parseInt(command[1]);
                getSlotNumbersOfDriversOfAge(driverAge);
                break;

            case Slot_number_for_car_with_number:
                regNum = command[1];
                getSlotNumberOfCarWithNumber(regNum);
                break;

            case Leave:
                slotNum = Integer.parseInt(command[1]);
                leaveParking(slotNum);
                break;

            case Vehicle_registration_number_for_driver_of_age:
                driverAge = Integer.parseInt(command[1]);
                getRegNumbersOfDriverAge(driverAge);
                break;
        }
    }

    public static void main(String args[]){

        ParkingLot obj = new ParkingLot();
        try
        {
            Scanner sc;
            FileInputStream fis = new FileInputStream(args[0]); // take the input file from argument
            sc = new Scanner(fis);

            while(sc.hasNextLine())
            {
                String str = sc.nextLine();
                String[] command = str.split("\\s+");
                obj.executeCommand(command); // execute the query.
            }
            sc.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
