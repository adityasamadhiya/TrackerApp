package org.tracker.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectLocationHistory {
    private List<TemporalCoordinate> temporalCoordinates;
    private String objectId;

    public ObjectLocationHistory(String objectId, List<TemporalCoordinate> temporalCoordinates) {
        this.objectId = objectId;
        this.temporalCoordinates = temporalCoordinates;
    }


    public void setJourneyHistory(List<TemporalCoordinate> temporalCoordinates) {
        this.temporalCoordinates = temporalCoordinates;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<TemporalCoordinate> getJourneyHistory() {
        return List.copyOf(temporalCoordinates);
    }

    public String getObjectId() {
        return objectId;
    }

    public void addJourney(TemporalCoordinate coordinate) {
        temporalCoordinates.add(coordinate);
    }

}
