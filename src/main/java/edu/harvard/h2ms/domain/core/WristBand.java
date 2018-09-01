package edu.harvard.h2ms.domain.core;

import javax.persistence.*;

/**
 * A Wrist Band is..
 */
@Entity
@Table(name = "WRIST_BAND")
public class WristBand {

    /* Properties */
    private Long id;
    private String sku;
    private String bitFormat;
    private String color;
    private int orderNumber;
    private boolean laserEngraved;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "SKU")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Column(name = "BIT_FORMAT")
    public String getBitFormat() {
        return bitFormat;
    }

    public void setBitFormat(String bitFormat) {
        this.bitFormat = bitFormat;
    }

    @Column(name = "COLOR")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Column(name = "ORDER_NUMBER")
    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Column(name = "LASER_ENGRAVED")
    public boolean isLaserEngraved() {
        return laserEngraved;
    }

    public void setLaserEngraved(boolean laserEngraved) {
        this.laserEngraved = laserEngraved;
    }

    @Override
    public String toString() {
        return "Wrist Band - Id: " + id + ", SKU: " + sku + ", Bit Format: " + bitFormat
                + ", Color: " + color + ", Order Number: " + orderNumber + ", Laser Engraved: " + laserEngraved;
    }

}
