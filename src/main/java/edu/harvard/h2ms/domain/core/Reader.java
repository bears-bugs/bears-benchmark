package edu.harvard.h2ms.domain.core;

import javax.persistence.*;

/**
 * A Reader is..
 */
@Entity
@Table(name = "READER")
public class Reader {

    /* Properties */
    private Long id;
    private int orderNumber;
    private String brandName;
    private String modelNumber;
    private String capacity;
    private String voltage;
    private String chargeTime;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "ORDER_NUMBER")
    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Column(name = "BRAND_NAME")
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Column(name = "MODEL_NUMBER")
    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    @Column(name = "CAPACITY")
    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Column(name = "VOLTAGE")
    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    @Column(name = "CHARGE_TIME")
    public String getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(String chargeTime) {
        this.chargeTime = chargeTime;
    }

    @Override
    public String toString() {
        return "Reader - Id: " + id + ", Order Number: " + orderNumber + ", Brand Name: " + brandName
                + ", Model Number: " + modelNumber + ", Capacity: " + capacity
                + ", Voltage: " + voltage + ", Charge Time: " + chargeTime;
    }

}
