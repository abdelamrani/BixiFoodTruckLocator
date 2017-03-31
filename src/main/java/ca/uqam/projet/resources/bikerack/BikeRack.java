package ca.uqam.projet.resources.bikerack;

import ca.uqam.projet.resources.Point;

/**
 * This is the effective bike rack class we use internally.
 */
public class BikeRack {

    private int id;

    private String brand;

    private Point coordinates;

    public BikeRack(int id, String brand, double longitude, double latitude) {
        this.id = id;
        this.brand = brand;
        this.coordinates = new Point(longitude, latitude);
    }

    public int getId() {
        return this.id;
    }

    public String getBrand() {
        return this.brand;
    }

    public Point getCoordinates() {
        return this.coordinates;
    }
}
