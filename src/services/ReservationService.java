package services;

import models.Customer;
import models.IRoom;
import models.Reservation;
import models.Room;

import java.util.*;

public class ReservationService {

    private static ReservationService instance;

    private final Collection<Reservation> reservations;
    private final Collection<IRoom> rooms;

    private ReservationService(){
        reservations = new HashSet<>();
        rooms = new HashSet<>();
    }

    public static ReservationService getInstance() {
        if(instance == null) {
            instance = new ReservationService();
        }
        return instance;
    }

    public Collection<IRoom> getRooms() {
        return rooms;
    }

    public void addRoom(IRoom room){
        rooms.add(room);
    }

    public IRoom getARoom(String roomId){
        IRoom roomFound = null;
        for(IRoom room: rooms){
            if(roomId.equals(room.getRoomNumber())){
                roomFound = room;
            }
        }
        return roomFound;
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkoutDate) {
        Reservation newReservation = new Reservation(customer, room, checkInDate, checkoutDate);
        reservations.add(newReservation);
        return newReservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        Set<IRoom> validRooms = new HashSet<>(this.rooms);

        for(Reservation reservation: reservations){
            boolean isCheckInValid = checkInDate.before(reservation.getCheckInDate()) || checkInDate.compareTo(reservation.getCheckOutDate()) >= 0;
            boolean isCheckOutValid = checkOutDate.after(reservation.getCheckOutDate()) || checkOutDate.compareTo(reservation.getCheckInDate()) <= 0;

            if(!isCheckInValid|| !isCheckOutValid) {
                validRooms.remove(reservation.getRoom());
            }

        }
        return new ArrayList<>(validRooms);
    }

    public Collection<Reservation> getCustomerReservation(Customer customer){
        List<Reservation> reserved = new ArrayList<>();
        for(Reservation reservation: reservations) {
            if(reservation.getCustomer().equals(customer)) {
                reserved.add(reservation);
            }
        }
        return reserved;
    }

    public void printAllReservation() {
        if(reservations.size() == 0) {
            System.out.println("No Reservations found.");
        } else {
            for(Reservation reservation: reservations){
                System.out.println(reservation);
            }
        }
    }
}
