package org.jsapar;

public class TstPostAddress {
    private String street;
    private String town;
    private TstPerson owner;
    private TstPostAddress subAddress;

    public TstPostAddress() {
        super();
    }

    public TstPostAddress(String street, String town) {
        super();
        this.street = street;
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setOwner(TstPerson owner) {
        this.owner = owner;
    }

    public TstPerson getOwner() {
        return owner;
    }

    public void setSubAddress(TstPostAddress subAddress) {
        this.subAddress = subAddress;
    }

    public TstPostAddress getSubAddress() {
        return subAddress;
    }
}
