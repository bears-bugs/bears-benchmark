package edu.harvard.h2ms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.usb.*;
import java.util.List;

/**
 * The RFID Scanner Service Implementor is
 */
public class RfidScannerServiceImpl implements RfidScannerService {

    private static final Logger log = LoggerFactory.getLogger(RfidScannerServiceImpl.class);

    /**
     * Searches through all human interface devices attached to machine
     * in search of specified RFID scanner.
     * @param usbHub - root hub for the services of the host manager
     * @param vendorIdentifier - vendor ID of the RFID scanner
     * @param productIdentifier - product ID of the RFID scanner
     * @return
     */
    public UsbDevice findAttachedRfidScanner(UsbHub usbHub, short vendorIdentifier, short productIdentifier)
    {
        List<UsbDevice> usbDevices = usbHub.getAttachedUsbDevices();
        for (UsbDevice device : usbDevices) {
            short deviceProductId = device.getUsbDeviceDescriptor().idProduct();
            short deviceVendorId = device.getUsbDeviceDescriptor().idVendor();

            if (deviceProductId == productIdentifier && deviceVendorId == vendorIdentifier){
                log.info("RFID Scanner Identified \n" + device.toString());
                return device;
            }

            if (device.isUsbHub()) {
                device = findAttachedRfidScanner((UsbHub) device, vendorIdentifier, productIdentifier);
                if (device != null) {
                    log.info("RFID Scanner Identified \n" + device.toString());
                    return device;
                }
            }

        }
        return null;
    }

    /**
     * Retrieves the communication interface of RFID scanner
     * @param usbRfidScanner - RFID scanner
     * @return
     */
    public UsbInterface findRfidScannerInterface(UsbDevice usbRfidScanner) {
        UsbConfiguration configuration = usbRfidScanner.getActiveUsbConfiguration();
        UsbInterface rfidScannerInterface =  configuration.getUsbInterface((byte) 0x00);
        log.info("RFID Scanner interface found: " + rfidScannerInterface.toString());
        return rfidScannerInterface;
    }

}
