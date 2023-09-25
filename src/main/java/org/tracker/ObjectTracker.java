package org.tracker;

import org.objectmatcher.implementation.ObjectMatcherUtil;
import org.objectmatcher.model.ObjectAggregate;
import org.objectmatcher.model.ObjectModel;
import org.tracker.model.Coordinate;
import org.tracker.model.ObjectEvent;
import org.tracker.model.TemporalCoordinate;
import org.tracker.model.ObjectLocationHistory;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ObjectTracker {

    private ObjectTrackerDao dao;

    public ObjectTracker(ObjectTrackerDao dao) {
        this.dao = dao;
    }

    public List<ObjectLocationHistory> trackObject(List<ObjectEvent> objectEvents) {
        dao.addAll(objectEvents);

        Long min = objectEvents.stream().mapToLong( o->o.getTemporalCoordinate().getTime()).min().getAsLong();
        Long max = objectEvents.stream().mapToLong( o->o.getTemporalCoordinate().getTime()).max().getAsLong();

        Long step = (long) (5 * 60);

        List<ObjectEvent> objectEvents1 = identifyAndTrackObjects(objectEvents, min, max, step);

        step = (long) (60 * 60);

        List<ObjectEvent> objectEvents2 = identifyAndTrackObjects(objectEvents1, min, max, step);

        step = (long) (24 * 60 * 60);

        List<ObjectEvent> objectEvents3 = identifyAndTrackObjects(objectEvents2, min, max, step);

        return getObjectLocationHistory(dao.getAll());
    }

    private List<ObjectLocationHistory> getObjectLocationHistory(HashMap<UUID, HashMap<Long, Coordinate>> locationHistory) {
        List<ObjectLocationHistory> history = new ArrayList<>();

        locationHistory.forEach( (uuid, locationMap)-> {
            List<TemporalCoordinate> temporalCoordinates = new ArrayList<>();
            locationMap.forEach( (time, coordinates) -> {
                temporalCoordinates.add(new TemporalCoordinate(coordinates, time));
            });
            history.add(new ObjectLocationHistory(uuid.toString(), temporalCoordinates));
            ;
        });

        return history;
    }

    private List<ObjectEvent> identifyAndTrackObjects(List<ObjectEvent> objectEvents, Long min, Long max, Long step) {
        List<ObjectEvent> groupedEvents = new ArrayList<>();
        ObjectMatcherUtil matcher = new ObjectMatcherUtil();

        HashMap<UUID, ObjectEvent> attributeObjectMap = new HashMap<>();

        objectEvents.forEach(objectEvent -> {
            attributeObjectMap.put(objectEvent.getUuid(), objectEvent);
        });


        for(Long index = min; index <= max; index = Instant.ofEpochMilli(index).plusSeconds(step).toEpochMilli()) {
            Long endTime = Instant.ofEpochMilli(index).plusSeconds(step).toEpochMilli();

            List<ObjectEvent> timeFilteredEvent = getFilteredEvents(objectEvents, index, endTime);
            List<ObjectModel> attributes = convertToObjectModel(timeFilteredEvent);

            List<ObjectAggregate> groupedAttributes = matcher.matchObject(attributes, 70.0);

            groupedEvents.addAll(getGroupedEventsFromGroupedAttributes(min, attributeObjectMap, groupedAttributes));

        }

        return groupedEvents;
    }

    private List<ObjectModel> convertToObjectModel(List<ObjectEvent> filteredEvent) {
        List<ObjectModel> objectModels = new ArrayList<>();
        filteredEvent.forEach( objectEvent -> {
            objectModels.add(new ObjectModel(objectEvent.getUuid().toString(), objectEvent.getAttributes()));
        });
        return objectModels;
    }

    private Collection<? extends ObjectEvent> getGroupedEventsFromGroupedAttributes(Long min, HashMap<UUID, ObjectEvent> attributeObjectMap, List<ObjectAggregate> aggregateAttributes) {
        List<ObjectEvent> objectEvents = new ArrayList<>();
        aggregateAttributes.forEach( objectAggregate -> {
            Long timeStamp = min;
            Double latitude = 0.0;
            Double longitude = 0.0;
            HashSet<UUID> uuids = new HashSet<>();
            for(ObjectModel att : objectAggregate.getObjectAttributes()) {
                ObjectEvent objectEvent = attributeObjectMap.get(UUID.fromString(att.getId()));
                uuids.add(objectEvent.getUuid());
                if(objectEvent.getTemporalCoordinate().getTime() > timeStamp) {
                    timeStamp = objectEvent.getTemporalCoordinate().getTime();
                    latitude = objectEvent.getTemporalCoordinate().getLatitude();
                    longitude = objectEvent.getTemporalCoordinate().getLongitude();
                }
            }
            ObjectEvent event = new ObjectEvent(new TemporalCoordinate(latitude, longitude, timeStamp), objectAggregate.getObjectAggregateAttributes());
            objectEvents.add(event);
            uuids.forEach( uuid -> {
                dao.update(uuid, event.getUuid());
            });

        });
        return objectEvents;

    }

    private List<List<Double>> attributeExtractor(List<ObjectEvent> filteredEvent) {
        return filteredEvent.stream()
                .map(ObjectEvent::getAttributes)
                .collect(Collectors.toList());
    }

    private List<ObjectEvent> getFilteredEvents(List<ObjectEvent> objectEvents, Long startTime, Long endTime) {
        return objectEvents.stream().filter( objectEvent -> {
            Long time = objectEvent.getTemporalCoordinate().getTime();
            return (time >= startTime && time < endTime) ? Boolean.TRUE : Boolean.FALSE;
        }).toList();
    }
}
