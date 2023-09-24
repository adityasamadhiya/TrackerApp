package org.tracker;

import org.junit.jupiter.api.Test;
import org.tracker.model.ObjectEvent;
import org.tracker.model.TemporalCoordinate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ObjectTrackerTest {

    @Test
    public void testIdentifyAndTrackObject() {
        List<ObjectEvent> events = new ArrayList<>();
        events.add(getObjectEvent(List.of(1.0, 2.0, 3.0, 4.0, 5.0), 52.5200, 13.4050, "2023-09-25T08:00:00Z"));
        events.add(getObjectEvent(List.of(1.0, 2.0, 3.0, 4.0, 6.0), 52.5300, 13.5000, "2023-09-25T08:01:00Z"));
        events.add(getObjectEvent(List.of(1.0, 2.0, 3.0, 4.0, 7.0), 53.5300, 14.5000, "2023-09-25T08:02:00Z"));
        events.add(getObjectEvent(List.of(1.0, 2.0, 3.0, 4.0, 10.0), 55.5300, 15.5000, "2023-09-25T08:09:00Z"));

        events.add(getObjectEvent(List.of(7.0, 8.0, 10.0, 20.0, 10.0), 55.5300, 15.5000, "2023-09-25T09:09:00Z"));

        events.add(getObjectEvent(List.of(5.0, 6.0, 7.0, 8.0, 9.0), 53.5300, 14.5000, "2023-09-25T08:00:00Z"));
        events.add(getObjectEvent(List.of(5.0, 6.0, 7.0, 8.0, 10.0), 53.5300, 14.5000, "2023-09-25T08:02:00Z"));
        events.add(getObjectEvent(List.of(5.0, 6.0, 7.0, 8.0, 11.0), 53.5300, 14.5000, "2023-09-25T08:07:00Z"));
        events.add(getObjectEvent(List.of(5.0, 6.0, 7.0, 8.0, 12.0), 53.5300, 14.5000, "2023-09-25T08:08:00Z"));

        new ObjectTracker(new ObjectTrackerDao()).identifyAndTrackObject(events);
    }

    private ObjectEvent getObjectEvent(List<Double> attributes, double latitude, double longitude, String timestamp) {
        TemporalCoordinate temporalCoordinate = new TemporalCoordinate(latitude, longitude, Instant.parse(timestamp).toEpochMilli());
        return new ObjectEvent(temporalCoordinate, attributes);
    }
}
