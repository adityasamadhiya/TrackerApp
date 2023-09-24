package org.tracker.model;

import java.util.List;
import java.util.UUID;

public class ObjectEvent {

    private UUID uuid;
    private TemporalCoordinate temporalCoordinate;
    private List<Double> attributes;

    public ObjectEvent(UUID uuid, TemporalCoordinate temporalCoordinate, List<Double> attributes) {
        uuid = uuid;
        this.temporalCoordinate = temporalCoordinate;
        this.attributes = attributes;
    }

    public ObjectEvent(TemporalCoordinate temporalCoordinate, List<Double> attributes) {
        uuid = UUID.randomUUID();
        this.temporalCoordinate = temporalCoordinate;
        this.attributes = attributes;
    }

    public TemporalCoordinate getTemporalCoordinate() {
        return temporalCoordinate;
    }

    public List<Double> getAttributes() {
        return List.copyOf(attributes);
    }

    public UUID getUuid() {
        return uuid;
    }
}
