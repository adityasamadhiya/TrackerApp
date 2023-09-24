package org.tracker.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectLocationHistory {
    private List<TemporalCoordinate> journeyHistory;
    private Integer objectId;

    public ObjectLocationHistory(Integer objectId) {
        this.objectId = objectId;
        journeyHistory = new ArrayList<>();
    }

    public void setJourneyHistory(List<TemporalCoordinate> journeyHistory) {
        this.journeyHistory = journeyHistory;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public List<TemporalCoordinate> getJourneyHistory() {
        return List.copyOf(journeyHistory);
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void addJourney(TemporalCoordinate coordinate) {
        journeyHistory.add(coordinate);
    }

}
