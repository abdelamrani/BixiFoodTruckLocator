package ca.uqam.projet.resources.bikestation;

public class BikeStationDataParseException extends Exception {
    public BikeStationDataParseException(String message) {
        super(message);
    }

    public BikeStationDataParseException(String message, Throwable throwable) {
        super(message, throwable);
    }

    @Override
    public String getMessage() {
        return String.format("Bike Station Data parse error: %s", super.getMessage());
    }
}
