package org.tracker.implementation;

import org.objectmatcher.ObjectMatcherInterface;
import org.objectmatcher.model.ObjectAggregate;
import org.objectmatcher.model.ObjectAttributes;
import org.tracker.IObjectTracker;
import org.tracker.dao.ObjectTrackerDao;
import org.tracker.model.Coordinate;
import org.tracker.model.ObjectEvent;
import org.tracker.model.TemporalCoordinate;
import org.tracker.model.ObjectLocationHistory;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ObjectTrackerImpl implements IObjectTracker {

    private ObjectTrackerDao dao;
    ObjectMatcherInterface matcher;

    public ObjectTrackerImpl(ObjectTrackerDao dao, ObjectMatcherInterface objectMatcher) {
        this.dao = dao;
        this.matcher = objectMatcher;
    }

    public List<ObjectLocationHistory> trackObjects(List<ObjectEvent> objectEvents) {
        dao.addAll(objectEvents);

        List<Long> steps = List.of(5 * 60L, 60 * 60L, 24 * 60 * 60L);

        for(Long step : steps) {
            objectEvents = identifyAndTrackObjects(objectEvents, step);
        }

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

    private List<ObjectEvent> identifyAndTrackObjects(List<ObjectEvent> objectEvents, Long step) {
        Long min = objectEvents.stream().mapToLong( o->o.getTemporalCoordinate().getTime()).min().getAsLong();
        Long max = objectEvents.stream().mapToLong( o->o.getTemporalCoordinate().getTime()).max().getAsLong();


        HashMap<UUID, ObjectEvent> attributeObjectMap = new HashMap<>();
        objectEvents.forEach(objectEvent -> {
            attributeObjectMap.put(objectEvent.getUuid(), objectEvent);
        });


        List<ObjectEvent> groupedEvents = new ArrayList<>();


        for(Long index = min; index <= max; index = Instant.ofEpochMilli(index).plusSeconds(step).toEpochMilli()) {
            Long endTime = Instant.ofEpochMilli(index).plusSeconds(step).toEpochMilli();

            List<ObjectEvent> timeFilteredEvent = getFilteredEvents(objectEvents, index, endTime);
            List<ObjectAttributes> attributes = extractAttributesFromEvents(timeFilteredEvent);

            List<ObjectAggregate> groupedAttributes = matcher.matchObject(attributes);

            groupedEvents.addAll(convertAttributeToEvents(attributeObjectMap, groupedAttributes));

        }

        return groupedEvents;
    }

    private List<ObjectAttributes> extractAttributesFromEvents(List<ObjectEvent> filteredEvent) {
        List<ObjectAttributes> objectAttributes = new ArrayList<>();
        filteredEvent.forEach( objectEvent -> {
            objectAttributes.add(new ObjectAttributes(objectEvent.getUuid().toString(), objectEvent.getAttributes()));
        });
        return objectAttributes;
    }

    private List<ObjectEvent> convertAttributeToEvents(HashMap<UUID, ObjectEvent> attributeObjectMap, List<ObjectAggregate> aggregateAttributes) {
        List<ObjectEvent> objectEvents = new ArrayList<>();
        aggregateAttributes.forEach( objectAggregate -> {

            Long timeStamp = Long.MIN_VALUE;
            Double latitude = 0.0;
            Double longitude = 0.0;
            HashSet<UUID> uuids = new HashSet<>();

            for(ObjectAttributes att : objectAggregate.getObjectAttributes()) {
                ObjectEvent objectEvent = attributeObjectMap.get(UUID.fromString(att.getId()));
                uuids.add(objectEvent.getUuid());
                if(objectEvent.getTemporalCoordinate().getTime() > timeStamp) {
                    timeStamp = objectEvent.getTemporalCoordinate().getTime();
                    latitude = objectEvent.getTemporalCoordinate().getCoordinate().getLatitude();
                    longitude = objectEvent.getTemporalCoordinate().getCoordinate().getLongitude();
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
