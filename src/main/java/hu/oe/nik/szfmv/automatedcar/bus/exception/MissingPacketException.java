package hu.oe.nik.szfmv.automatedcar.bus.exception;

public class MissingPacketException extends Exception {

    /**
     * Custom exception. Throws when packet missing from Virtual Function Bus
     *
     * @param message message
     */
    public MissingPacketException(String message) {
        super(message);
    }
}
