package org.tracker;

import org.objectmatcher.implementation.ObjectMatcherUtil;
import org.objectmatcher.model.ObjectAggregate;
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

    public ObjectLocationHistory identifyAndTrackObject(List<ObjectEvent> objectEvents) {
        dao.addAll(objectEvents);

        Long min = objectEvents.stream().mapToLong( o->o.getTemporalCoordinate().getTime()).min().getAsLong();
        Long max = objectEvents.stream().mapToLong( o->o.getTemporalCoordinate().getTime()).max().getAsLong();

        Long step = (long) (5 * 60);

        List<ObjectEvent> objectEvents1 = sliceEventsAndIdentify(objectEvents, min, max, step);


        step = (long) (24 * 60 * 60);

        List<ObjectEvent> objectEvents2 = sliceEventsAndIdentify(objectEvents1, min, max, step);

        step = (long) (24 * 60 * 60);

        List<ObjectEvent> objectEvents3 = sliceEventsAndIdentify(objectEvents2, min, max, step);

        objectEvents.forEach(s -> System.out.println(s.getUuid()));
        System.out.println("-------------");
        objectEvents1.forEach(s -> System.out.println(s.getUuid()));
        System.out.println("--------------");
        dao.getAll().forEach(s -> System.out.println(s));

        return null;
    }

    private List<ObjectEvent> sliceEventsAndIdentify(List<ObjectEvent> objectEvents, Long min, Long max, Long step) {
        HashMap<Integer, ObjectEvent> attributeObjectMap = new HashMap<>();
        List<ObjectEvent> updatedObjectEvent = new ArrayList<>();
        ObjectMatcherUtil matcher = new ObjectMatcherUtil();

        objectEvents.forEach(objectEvent -> {
            attributeObjectMap.put(objectEvent.getAttributes().hashCode(), objectEvent);
        });


        for(Long index = min; index <= max; index = Instant.ofEpochMilli(index).plusSeconds(step).toEpochMilli()) {
            Long endTime = Instant.ofEpochMilli(index).plusSeconds(step).toEpochMilli();
            Long startTime = index;

            List<ObjectEvent> filteredEvent = getFilteredEvents(objectEvents, startTime, endTime);

            List<List<Double>> attributes = attributeExtractor(filteredEvent);
            List<ObjectAggregate> aggregateAttributes = matcher.matchObject(attributes, 70.0);

            updatedObjectEvent.addAll(getUpdatedEventsFromAttributes(min, attributeObjectMap, aggregateAttributes));
//
//            aggregateAttributes.forEach( objectAggregate -> {
//                Long timeStamp = min;
//                Double latitude = 0.0;
//                Double longitude = 0.0;
//                HashSet<UUID> uuids = new HashSet<>();
//                for(List<Double> att : objectAggregate.getObjectAttributes()) {
//                    ObjectEvent objectEvent = attributeObjectMap.get(att);
//                    uuids.add(objectEvent.getUuid());
//                    if(objectEvent.getTemporalCoordinate().getTime() > timeStamp) {
//                        timeStamp = objectEvent.getTemporalCoordinate().getTime();
//                        latitude = objectEvent.getTemporalCoordinate().getLatitude();
//                        longitude = objectEvent.getTemporalCoordinate().getLongitude();
//                    }
//                }
//                ObjectEvent event = new ObjectEvent(new TemporalCoordinate(latitude, longitude, timeStamp), objectAggregate.getObjectAggregateAttributes());
//                updatedObjectEvent.add(event);
//                uuids.forEach( uuid -> {
//                    dao.update(uuid, event.getUuid());
//                });
//                System.out.println("Hi");
//
//            });

        }
        return updatedObjectEvent;
    }

    private Collection<? extends ObjectEvent> getUpdatedEventsFromAttributes(Long min, HashMap<Integer, ObjectEvent> attributeObjectMap, List<ObjectAggregate> aggregateAttributes) {
        List<ObjectEvent> objectEvents = new ArrayList<>();
        aggregateAttributes.forEach( objectAggregate -> {
            Long timeStamp = min;
            Double latitude = 0.0;
            Double longitude = 0.0;
            HashSet<UUID> uuids = new HashSet<>();
            for(List<Double> att : objectAggregate.getObjectAttributes()) {
                ObjectEvent objectEvent = attributeObjectMap.get(att.hashCode());
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
            System.out.println("Hi");

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
