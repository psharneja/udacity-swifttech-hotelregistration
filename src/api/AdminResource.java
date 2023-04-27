package api;

import models.Customer;
import models.IRoom;
import services.CustomerService;
import services.ReservationService;

import java.util.*;

public class AdminResource {

    private static AdminResource instance;
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    public static AdminResource getInstance() {
        if(instance == null) {
            instance = new AdminResource();
        }
        return instance;
    }

    public AdminResource(){}


    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms){
        for (IRoom room: rooms){
            reservationService.addRoom(room);
        }
    }

    public Collection<IRoom> getAllRooms() {
        Collection<IRoom> rooms = reservationService.getRooms();
        return new ArrayList<>(rooms);
    }

    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    public void displayAllReservations(){
        reservationService.printAllReservation();
    }

}
