package org.tracker;

import org.tracker.model.Coordinate;
import org.tracker.model.ObjectEvent;
import org.tracker.model.TemporalCoordinate;

import java.util.*;

public class ObjectTrackerDao {
    private HashMap<UUID, HashMap<Long, Coordinate>> locationTable;

    public ObjectTrackerDao() {
        this.locationTable = new HashMap<>();
    }

    public void add(UUID uuid, Long timestamp, Double latitude, Double longitude) {
        Coordinate coordinate = new Coordinate(latitude, longitude);
        add(uuid, timestamp, coordinate);
    }

    public void add(UUID uuid, Long timestamp, Coordinate coordinate){
        locationTable.computeIfAbsent(uuid, key -> new HashMap<>()).put(timestamp, coordinate);
    }

    public void update(UUID uuid, Long timestamp, Coordinate coordinate){
        locationTable.computeIfAbsent(uuid, key -> new HashMap<>()).put(timestamp, coordinate);
    }

    public void update(UUID sourceUUID, UUID destinationUUI) {
        HashMap<Long, Coordinate> timeCoordinate = locationTable.getOrDefault(sourceUUID, new HashMap<>());
        locationTable.computeIfAbsent(destinationUUI, key -> new HashMap<>()).putAll(timeCoordinate);
        locationTable.remove(sourceUUID);
    }

    public void addAll(List<ObjectEvent> objectEvents) {
        objectEvents.forEach(objectEvent -> {
            locationTable.computeIfAbsent(objectEvent.getUuid(), key -> new HashMap<>()).put(objectEvent.getTemporalCoordinate().getTime(), objectEvent.getTemporalCoordinate().getCoordinate());
        });
    }

    public List<ObjectEvent> getAll() {
        throw new UnsupportedOperationException("Method not supported");
    }

}
