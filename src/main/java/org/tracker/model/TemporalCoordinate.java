package org.tracker.model;

public class TemporalCoordinate {

    private Coordinate coordinate;
    private Long time;

    public TemporalCoordinate(Double latitude, Double longitude, Long time) {
        this.coordinate = new Coordinate(latitude, longitude);
        this.time = time;
    }

    public Double getLatitude() {
        return coordinate.getLatitude();
    }

    public Double getLongitude() {
        return coordinate.getLongitude();
    }

    public Long getTime() {
        return time;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
