package models;

import java.util.regex.Pattern;

public class Customer {
    private final String firstName;
    private final String lastName;
    private final String email;

    public Customer (String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        if(isEmailValid(email)){
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format, Please enter a valid email.");
        }
    }

    private boolean isEmailValid(String email){
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        return emailPattern.matcher(email).matches();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Customer: " + firstName + " " + lastName + ", " + email + ".";
    }


    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 31 * hash + email.hashCode();
        hash = 31 * hash + firstName.hashCode();
        hash = 31 * hash +lastName.hashCode();
        return hash;
    }

    @Override
    public final boolean equals(Object o){
        if(o == this){
            return true;
        }
        if(o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return email.equals(customer.email);
    }

}
