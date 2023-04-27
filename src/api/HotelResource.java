package api;

import models.Customer;
import models.IRoom;
import models.Reservation;
import services.CustomerService;
import services.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    private static HotelResource instance;
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    public HotelResource(){}


    public static HotelResource getInstance() {
        if(instance == null) {
            instance = new HotelResource();
        }
        return instance;
    }


    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName){
        customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate){
        return reservationService.reserveARoom(customerService.getCustomer(customerEmail), room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail) {
        return reservationService.getCustomerReservation(customerService.getCustomer(customerEmail));
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut) {
        return reservationService.findRooms(checkIn, checkOut);
    }



}
