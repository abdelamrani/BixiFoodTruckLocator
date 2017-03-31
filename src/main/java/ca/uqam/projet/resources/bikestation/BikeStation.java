package ca.uqam.projet.resources.bikestation;

import ca.uqam.projet.resources.Point;

/**
 * This is the effective bike station class we use internally.
 */
public class BikeStation {

    private int id;

    private String name;

    private int bikeCount;

    private int emptySpacesCount;

    private Point coordinates;

    public BikeStation(int id, String name, int bikeCount, int emptySpaces, double longitude, double latitude) {
        this.id = id;
        this.name = name;
        this.bikeCount = bikeCount;
        this.emptySpacesCount = emptySpaces;
        this.coordinates = new Point(longitude, latitude);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getBikeCount() {
        return this.bikeCount;
    }

    public int getEmptySpacesCount() {
        return this.emptySpacesCount;
    }

    public Point getCoordinates() {
        return this.coordinates;
    }
}
