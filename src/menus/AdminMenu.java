package menus;

import api.AdminResource;
import api.HotelResource;
import models.*;

import java.util.*;

public class AdminMenu {
    private AdminResource adminResource = AdminResource.getInstance();
    private HotelResource hotelResource = HotelResource.getInstance();
    Scanner scanner = new Scanner(System.in);

    AdminMenu(){
    }

    public void adminMenu() {
        String line = "";
        boolean menuRunning = true;

        try {
            System.out.println("Welcome to the Hotel");

            do {
                printOptions();
                line = scanner.nextLine();

                switch (line.toString()) {
                    case "1" -> allCustomer();
                    case "2" -> allRooms();
                    case "3" -> allReservations();
                    case "4" -> addRoom();
                    case "5" -> {
                        System.out.println("Bye admins!");
                        menuRunning = false;
                    }
                    case "6" -> populateTestData();
                    default -> System.out.println("Please enter a number between 1 and 5.");
                }
                Thread.sleep(200);
                System.out.println("...");
            } while (menuRunning);
        } catch(Exception e) {
            System.out.println("An error occurred, exiting...");
        }

    }

    private void printOptions(){
        System.out.println(
                """
                        ---------------------------------\s
                        1. See all Customers\s
                        2. See all Rooms\s
                        3. See all Reservations\s
                        4. Add a Room\s
                        5. Back to Main Menu\s
                        6. Populate Test Data\s
                        ---------------------------------\s
                        Please enter a number to select from the options\s
                        """
        );


    }

    private void populateTestData(){
        hotelResource.createACustomer("foobar@email.com","Foo", "Bar");
        hotelResource.createACustomer("pengwen@email.com","Pen", "Gwen" );
        hotelResource.createACustomer("johnsmith@email.com", "John", "Smith" );


        List<IRoom> rooms = new ArrayList<>();
        rooms.add(new FreeRoom("111", RoomType.SINGLE));
        rooms.add(new Room("222", 123.0, RoomType.DOUBLE));
        rooms.add(new Room("333", 99.0, RoomType.SINGLE));
        adminResource.addRoom(rooms);

        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, 1);
        Date tomorrow = calendar.getTime();
        hotelResource.bookARoom("foobar@email.com", hotelResource.getRoom("111"), today,tomorrow );

        calendar.add(Calendar.DATE, 3);
        Date threeDays = calendar.getTime();

        calendar.add(Calendar.DATE, 5);
        Date fiveDays = calendar.getTime();

        hotelResource.bookARoom("pengwen@email.com", hotelResource.getRoom("111"), threeDays,fiveDays );

        hotelResource.bookARoom("johnsmith@email.com", hotelResource.getRoom("222"), tomorrow,fiveDays );
    }

    private void allCustomer(){
        Collection<Customer> customers = adminResource.getAllCustomers();
        if(customers.isEmpty()){
            System.out.println("No Customers, add one form the main menu...");
        } else {
            for(Customer customer: customers){
                System.out.println(customer);
            }
        }
    }
    private void allRooms(){
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if(rooms.isEmpty()){
            System.out.println("No rooms in a system, please add from the menu...");
        } else {
            for(IRoom room: rooms) {
                System.out.println(room);
            }
        }
    }
    private void allReservations(){
        adminResource.displayAllReservations();
    }
    private void addRoom(){

        boolean moveAhead = false;
        do{
            List<IRoom> rooms = (List<IRoom>) adminResource.getAllRooms();
            List<IRoom> newRooms = new ArrayList<>();
            String roomNumber = enterRoomNumber(rooms);
            RoomType roomType = enterRoomType();
            boolean freeRoom = confirmFreeRoom();
            if(freeRoom){
                newRooms.add(new FreeRoom(roomNumber, roomType));
            } else{
                double roomPrice = enterRoomPrice();
                newRooms.add(new Room(roomNumber, roomPrice, roomType));
            }
            System.out.println("Add another room?, enter `Y` for YES or any key for NO");
            String  answer = scanner.nextLine();
            switch (answer){
                case "y", "Y" ->  {}
                case "n", "N" ->  moveAhead = true;
                default ->  moveAhead = true;
            }
            adminResource.addRoom(newRooms);
        }while(!moveAhead);
    }

    private boolean confirmFreeRoom(){
        System.out.println("Do you want to make this room available to members for free?");
        boolean isFree = false;
        boolean moveAhead = false;
        do{
            String answer = scanner.nextLine();
            switch (answer){
                case "y" -> {isFree= true;moveAhead=true;}
                case "n" -> {moveAhead = true;}
                default -> System.out.println("please confirm by y/n for yes or no");
            }

        }while(!moveAhead);
        return isFree;

    }


    private RoomType enterRoomType(){
        RoomType roomType = null;
        boolean moveAhead = false;
        System.out.println("Choose type of room: 1. Single, 2. double");
        do{
            String answer  = scanner.nextLine();
            switch (answer){
                case "1" -> {roomType = RoomType.SINGLE; moveAhead= true;}
                case "2" -> {roomType = RoomType.DOUBLE; moveAhead= true;}
                default -> System.out.println("Invalid input, try again.");
            }
        }while(!moveAhead);
        return roomType;

    }


    private double enterRoomPrice(){
        System.out.println("Enter price for the room:");
        boolean roomPriceValid = false;
        String answer = "";
        do{
            answer = scanner.nextLine();
            try{
                Double.parseDouble(answer);
                roomPriceValid = true;
            }catch(NumberFormatException e){
                System.out.println("Not a valid amount, try again...");
            }
        }while(!roomPriceValid);
        return Double.parseDouble(answer);
    }

    private String enterRoomNumber(List<IRoom> rooms){
        System.out.println("Give your room a number...");
        String answer = "";
        boolean roomNumberValid = false;

        do{
            answer = scanner.nextLine();
            try{
                Integer.parseInt(answer);
                if(roomNumberExists(answer, rooms)){
                    System.out.println("There already exists a room with the same number, try again.");
                    continue;
                }
                roomNumberValid = true;
            }catch(NumberFormatException e){
                System.out.println("Please enter a valid room number");
            }

        }while(!roomNumberValid);

        return answer;
    }

    private boolean roomNumberExists(String answer, List<IRoom> rooms){
        for(IRoom room: rooms){
            if(room.getRoomNumber().equals(answer)){
                return true;
            }
        }
        return false;
    }

}
