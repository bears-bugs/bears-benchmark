/** 
 * Copyright: Jonas Stenberg
 */
package org.jsapar;

import java.util.Date;

/**
 * Utility class for the tests. This class is used by the test classes.
 * 
 * @author Jonas Stenberg
 * 
 */
public class TstPerson {

    private String         firstName;
    private String         lastName = "Nobody";
    private short          shoeSize;
    private long           luckyNumber;
    private int            streetNumber;
    private char           door;
    private boolean        adult;
    private TstPostAddress    address;
    private TstPostAddress    workAddress;
    private double         length;
    private TstGender      gender;
    
    private java.util.Date birthTime;

    public TstPerson(String firstName, String lastName, short shoeSize, long luckyNumber, Date birthTime, int streetNumber, char door) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.shoeSize = shoeSize;
        this.setStreetNumber(streetNumber);
        this.luckyNumber = luckyNumber;
        this.birthTime = birthTime;
        this.door = door;
    }

    /**
     * 
     */
    public TstPerson() {
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the shoeSize
     */
    public short getShoeSize() {
        return shoeSize;
    }

    /**
     * @param shoeSize
     *            the shoeSize to set
     */
    public void setShoeSize(short shoeSize) {
        this.shoeSize = shoeSize;
    }

    /**
     * @return the happyNumber
     */
    public long getLuckyNumber() {
        return luckyNumber;
    }

    public void setLuckyNumber(String theNumber) {
        this.luckyNumber = Long.valueOf(theNumber);
    }

    /**
     * @param happyNumber
     *            the happyNumber to set
     */
    public void setLuckyNumber(long happyNumber) {
        this.luckyNumber = happyNumber;
    }

    /**
     * @return the birthTime
     */
    public java.util.Date getBirthTime() {
        return birthTime;
    }

    /**
     * @param birthTime
     *            the birthTime to set
     */
    public void setBirthTime(java.util.Date birthTime) {
        this.birthTime = birthTime;
    }

    /**
     * @param streetNumber the streetNumber to set
     */
    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    /**
     * @return the streetNumber
     */
    public int getStreetNumber() {
        return streetNumber;
    }

    /**
     * @param door the door to set
     */
    public void setDoor(char door) {
        this.door = door;
    }

    /**
     * @return the door
     */
    public char getDoor() {
        return door;
    }

    public void setAddress(TstPostAddress address) {
        this.address = address;
        address.setOwner(this);
    }

    public TstPostAddress getAddress() {
        return address;
    }

    public void setWorkAddress(TstPostAddress workAddress) {
        this.workAddress = workAddress;
        address.setOwner(this);
    }

    public TstPostAddress getWorkAddress() {
        return workAddress;
    }

    /**
     * @return the length
     */
    public double getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(double length) {
        this.length = length;
    }

    public TstGender getGender() {
        return gender;
    }

    public void setGender(TstGender gender) {
        this.gender = gender;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }
}
