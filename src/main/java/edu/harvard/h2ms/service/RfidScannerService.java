package edu.harvard.h2ms.service;

import javax.usb.UsbDevice;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;

/**
 * The RFID Scanner Service...
 */
public interface RfidScannerService {

    /**
     * Searches through all human interface devices attached to machine
     * in search of specified RFID reader provided in parameters.
     * @param usbHub - root hub for the services of the host manager
     * @param vendorIdentifier - vendor ID of the RFID Reader device
     * @param productIdentifier - product ID of the RFID Reader device
     * @return
     */
    UsbDevice findAttachedRfidScanner(UsbHub usbHub, short vendorIdentifier, short productIdentifier);

    /**
     * Retrieves the communication interface of RFID scanner
     * @param usbRfidScanner - RFID scanner
     * @return
     */
    UsbInterface findRfidScannerInterface(UsbDevice usbRfidScanner);

}
