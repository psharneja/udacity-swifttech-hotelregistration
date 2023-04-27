package menus;

import api.HotelResource;
import models.IRoom;
import models.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class MainMenu {

    public final AdminMenu adminMenu = new AdminMenu();
    private final HotelResource hotelResource = HotelResource.getInstance();

    Scanner scanner = new Scanner(System.in);

    public MainMenu(){
    }

    public void mainMenu(){
        String line = "";
        boolean appRunning = true;

        try {
            System.out.println("Welcome to the Hotel");

            do {
                printOptions();
                line = scanner.nextLine();

                switch (line.toString()) {
                    case "1" -> reserveRoom();
                    case "2" -> myReservation();
                    case "3" -> createAccount();
                    case "4" -> adminMenu.adminMenu();
                    case "5" -> {
                        System.out.println("Bye!");
                        appRunning = false;
                    }
                    default -> System.out.println("Please enter a number between 1 and 5.");
                }
                Thread.sleep(200);
                System.out.println("..");
                Thread.sleep(300);
                System.out.println("...");
                Thread.sleep(200);
            } while (appRunning);
        } catch(Exception e) {
            System.out.println("An error occurred, exiting...");
        }
    }

    private void printOptions(){
        System.out.println(
                """
                        ---------------------------------\s
                        1. Find and reserve a room\s
                        2. See my reservations\s
                        3. Create an account\s
                        4. Admin\s
                        5. Exit\s
                        ---------------------------------\s
                        Please enter a number to select from the options\s
                        """
        );


    }

    private void createAccount() {
        boolean moveAhead = false;
        do{
            System.out.println("Enter your email: eg. name@domain.com");
            String email = getUserEmail();

            if(!accountNotFound(email)){
                System.out.println("Account with the email already exists, perhaps use a different email?");
                continue;
            }
            System.out.println(" Enter firstname (should start with a capital letter)");
            String firstName = getNameValue();
            System.out.println(" Enter lastname (should start with a capital letter)");
            String lastName = getNameValue();
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Account created.");
            moveAhead = true;
        }while(!moveAhead);
    }

    private String getNameValue() {
        String name = "";
        String answer;
        String nameRegex = "^[A-Z][a-zA-z ]{1,29}$";
        Pattern namePattern = Pattern.compile(nameRegex);

        boolean moveAhead = false;
        do{
            answer = scanner.nextLine();
            if(namePattern.matcher(answer).matches()){
                moveAhead = true;
                name = answer;
            } else {
                System.out.println("Enter a valid name");
            }
        } while(!moveAhead);
        return name;
    }


    private void myReservation() {
        System.out.println("Please enter the account email...");
        String email = getUserEmail();

        if(accountNotFound(email)){
            System.out.println("Account with the email doesn't exist, please create an account in the main menu first...");
            return;
        }

        Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);
        if(reservations.isEmpty()){
            System.out.println("No Reservations for you, please reserve a room from the main menu...");
        } else {
            for(Reservation reservation : reservations){
                System.out.println(reservation);
            }
        }
    }

    private void reserveRoom(){
        boolean findRoom = true;

        while(findRoom){
            System.out.println("Enter check-in date in dd/MM/yyyy format");
            Date checkIn = getValidDate();
            System.out.println("Enter check-out date in dd/MM/yyyy format");
            Date checkOut = getValidDate();
            if(checkIn.after(checkOut)){
                System.out.println("Check-In date can not be after the Check-out Date.");
                continue;
            }

            System.out.println("After how many days gap should we check if room not available for the above dates?");
            int differentiator  = getDateDifferentiator();
            boolean freeRoom = getFreeRoom();
            Collection<IRoom> vacantRooms  = findVacantRooms(checkIn, checkOut);
            if(vacantRooms.isEmpty()){
                System.out.println("No rooms found for the required set of dates, trying to find a room for the later date");
                checkIn = moveDate(checkIn, differentiator);
                checkOut = moveDate(checkOut, differentiator);
                vacantRooms  = findVacantRooms(checkIn, checkOut);
            }

            if(vacantRooms.isEmpty()) {
                findRoom = false;
                System.out.println("Please start again from the main menu...");
                continue;
            }
            if(freeRoom){
                List<IRoom> freeRooms =  new ArrayList<>();
                for(IRoom room: vacantRooms) {
                    if(room.isFree()){
                        freeRooms.add(room);
                    }
                }
                if(freeRooms.isEmpty()){
                    System.out.println("No rooms found for the required set of details");
                    findRoom = false;
                    continue;
                } else {
                    vacantRooms = freeRooms;
                    System.out.println("You can book below rooms between dates " + checkIn + " - " + checkOut);
                    for(IRoom room: freeRooms) {
                        System.out.println(room);
                    }
                }

            } else {

            System.out.println("You can book below rooms between dates " + checkIn + " - " + checkOut);
            for(IRoom room: vacantRooms) {
                System.out.println(room);
            }
            }

            if(!booking()){
                findRoom = false;
                continue;
            }

            if(noAccount()){
                findRoom = false;
                continue;
            }

            System.out.println("Enter email for the existing account...");
            String email = getUserEmail();

            if(accountNotFound(email)){
                System.out.println("Account with the email doesn't exist, please create an account in the main menu first...");
                findRoom = false;
                continue;
            }

            String roomNumberAnswer = bookRoomNumber(vacantRooms);
            IRoom room = hotelResource.getRoom(roomNumberAnswer);
            Reservation reservation = hotelResource.bookARoom(email, room, checkIn, checkOut);
            System.out.println(reservation);
            findRoom = false;

        }
    }

    private boolean getFreeRoom(){
        System.out.println("Do you want to book a free room?: (y/n)");
        boolean freeRoom = false;
        boolean moveAhead = false;
        do{
            String answer = scanner.nextLine();
            switch (answer.toLowerCase()){
                case "y" -> {moveAhead = true; freeRoom = true;}
                case "n" -> {moveAhead = true; freeRoom = false;}
                default -> System.out.println("Type `y` or `n` to confirm");
            }
        }while(!moveAhead);
        return freeRoom;
    }

    private Collection<IRoom> findVacantRooms(Date checkIn, Date checkOut) {
        return hotelResource.findARoom(checkIn, checkOut);
    }


    private Integer getDateDifferentiator(){
        boolean moveAhead = false;
        int diff = 7;
        do{
            String answer = scanner.nextLine();
            try{
                diff = Integer.parseInt(answer);
                moveAhead = true;
            } catch(NumberFormatException e){
                System.out.println("Please enter a valid number...");
            }
        }while(!moveAhead);
        return diff;

    }


    private String bookRoomNumber(Collection<IRoom> vacantRooms){
        System.out.println("Please enter the room number to book:");
        String roomNumber = "";
        boolean moveAhead = false;
        do{
            String answer = scanner.nextLine();
            try{
                boolean isRoomVacant = false;
                for(IRoom room: vacantRooms){
                    if(room.getRoomNumber().equals(answer)){
                        isRoomVacant = true;
                        break;
                    }
                }
                if(isRoomVacant){
                    moveAhead = true;
                    roomNumber = answer;
                } else {
                    System.out.println("please enter a valid room number;");
                }


            } catch(NumberFormatException e){
                System.out.println("Please enter a valid room number...");
            }
        }while(!moveAhead);
        return roomNumber;
    }


    private boolean accountNotFound(String email){
        return hotelResource.getCustomer(email) == null;
    }

    private String getUserEmail() {
        boolean moveAhead = false;
        String email = "";
        do{
            String answer = scanner.nextLine();
            if(isEmailInvalid(answer)){
                System.out.println("Not a valid email address, try again.");
                continue;
            }
            email = answer;
            moveAhead = true;
        }while(!moveAhead);
        return  email;
    }


    private boolean isEmailInvalid(String email){
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        return !emailPattern.matcher(email).matches();
    }

    private boolean noAccount() {
        System.out.println("Do you hold an account with the hotel? Type `y` or `n` to confirm ");
        boolean noAccount = true;
        boolean moveAhead = false;
        do{
            String answer = scanner.nextLine();
            switch (answer.toLowerCase()){
                case "y" -> {moveAhead = true; noAccount = false;}
                case "n" -> {
                    System.out.println("Please create an account in the main menu before proceeding:");
                    moveAhead = true;
                }
                default -> System.out.println("Type `y` or `n` to confirm");
            }
        }while(!moveAhead);
        return noAccount;
    }

    private boolean booking(){
        System.out.println("Do you want to book from the above mentioned rooms?: (y/n)");
        boolean book = true;
        boolean moveAhead = false;
        do{
            String answer = scanner.nextLine();
            switch (answer.toLowerCase()){
                case "y" -> moveAhead = true;
                case "n" -> book = false;
                default -> System.out.println("Type `y` or `n` to confirm");
            }
        }while(!moveAhead && book);
        return book;
    }

    private Date getValidDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;

        boolean readDate = true;
        do{
            String input = scanner.nextLine();
            try{
                date = dateFormat.parse(input);
                Date today = new Date();
                if(date.before(today)){
                    System.out.println("Entered date can only be in the future.");
                } else {
                    readDate = false;
                }
            } catch (ParseException e) {
                System.out.println("Invalid date format, please use dd/mm/yyyy");
            }

        }while(readDate);
        return date;
    }

    private Date moveDate(Date date, int differentiator) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, differentiator);
        return calendar.getTime();
    }



}
