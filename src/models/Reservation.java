package models;

import java.util.Date;

public class Reservation {

    private final Customer customer;
    private final IRoom room;
    private final Date checkInDate;
    private final Date checkOutDate;




    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString() {
        return "Reservation details: " + System.lineSeparator() +
                customer + System.lineSeparator() +
                room + System.lineSeparator() +
                "Dates: " + checkInDate + " - " + checkOutDate + ".";
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public IRoom getRoom() {
        return room;
    }

}


