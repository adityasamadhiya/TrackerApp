package org.tracker;

import org.junit.jupiter.api.Test;
import org.tracker.model.ObjectEvent;
import org.tracker.model.ObjectLocationHistory;
import org.tracker.model.TemporalCoordinate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ObjectTrackerTest {

    @Test
    public void testIdentifyAndTrackObject() {
        List<ObjectEvent> events = new ArrayList<>();
//       Object 1
        events.add(getObjectEvent(List.of(1.0, 2.0, 3.0, 4.0, 5.0), 52.5200, 13.4050, "2023-09-25T08:00:00Z"));
        events.add(getObjectEvent(List.of(1.0, 2.0, 3.0, 4.0, 6.0), 52.5300, 13.5000, "2023-09-25T08:01:00Z"));
        events.add(getObjectEvent(List.of(1.0, 2.0, 3.0, 4.0, 7.0), 53.5300, 14.5000, "2023-09-25T08:02:00Z"));
        events.add(getObjectEvent(List.of(1.0, 2.0, 3.0, 4.0, 10.0), 55.5300, 15.5000, "2023-09-25T08:09:00Z"));
//      Object 2
        events.add(getObjectEvent(List.of(7.0, 8.0, 10.0, 20.0, 10.0), 55.5300, 15.5000, "2023-09-25T09:09:00Z"));

//       Object 3
        events.add(getObjectEvent(List.of(5.0, 6.0, 7.0, 8.0, 9.0), 53.5300, 14.5000, "2023-09-25T08:00:00Z"));
        events.add(getObjectEvent(List.of(5.0, 6.0, 7.0, 8.0, 10.0), 53.5300, 14.5000, "2023-09-25T08:02:00Z"));
        events.add(getObjectEvent(List.of(5.0, 6.0, 7.0, 8.0, 11.0), 53.5300, 14.5000, "2023-09-25T08:07:00Z"));
        events.add(getObjectEvent(List.of(5.0, 6.0, 7.0, 8.0, 12.0), 53.5300, 14.5000, "2023-09-25T08:08:00Z"));

//       Object 4

        events.add(getObjectEvent(List.of(10.0, 12.0, 15.0, 22.0, 31.0), 93.6300, -23.4000, "2023-09-25T10:01:30Z"));
        events.add(getObjectEvent(List.of(10.0, 12.0, 15.0, 23.0, 31.0), 95.4300, -19.4100, "2023-09-25T10:04:30Z"));
        events.add(getObjectEvent(List.of(10.0, 12.0, 15.0, 32.0, 31.0), 97.2300, -13.4000, "2023-09-25T10:05:30Z"));
        events.add(getObjectEvent(List.of(10.0, 12.0, 15.0, 21.0, 31.0), 87.5300, -03.4000, "2023-09-25T10:06:30Z"));
        events.add(getObjectEvent(List.of(10.0, 12.0, 15.0, 19.0, 31.0), 86.1300, -13.4000, "2023-09-25T10:07:30Z"));
        events.add(getObjectEvent(List.of(10.0, 12.0, 15.0, 21.0, 31.0), 86.1300, -13.4000, "2023-09-25T10:37:30Z"));

//       Object 5

        events.add(getObjectEvent(List.of(13.0, 20.0, 15.0, 100.0, 68.0), 145.6300, 200.4000, "2023-09-25T18:31:30Z"));
        events.add(getObjectEvent(List.of(13.0, 20.0, 15.0, 100.0, 69.0), 146.6300, 201.4000, "2023-09-25T18:41:30Z"));
        events.add(getObjectEvent(List.of(13.0, 20.0, 15.0, 100.0, 70.0), 149.6300, 203.6000, "2023-09-25T19:11:30Z"));
        events.add(getObjectEvent(List.of(13.0, 20.0, 15.0, 100.0, 71.0), 141.6300, 210.9000, "2023-09-25T20:31:30Z"));

//       Object 6
        events.add(getObjectEvent(List.of(20.0, 50.0, 32.0, 100.0, 68.0), 78.6300, 98.4000, "2023-09-25T10:39:30Z"));
        events.add(getObjectEvent(List.of(20.0, 50.0, 32.0, 100.0, 68.0), 79.6300, 32.4000, "2023-09-25T18:54:30Z"));
        events.add(getObjectEvent(List.of(20.0, 50.0, 32.0, 100.0, 68.0), 87.6300, 45.4000, "2023-09-25T19:01:30Z"));
        events.add(getObjectEvent(List.of(20.0, 50.0, 32.0, 100.0, 68.0), 43.6300, 78.4000, "2023-09-25T06:29:30Z"));
        events.add(getObjectEvent(List.of(20.0, 50.0, 32.0, 100.0, 68.0), 21.6300, 21.4000, "2023-09-25T13:33:30Z"));
        events.add(getObjectEvent(List.of(20.0, 50.0, 32.0, 100.0, 68.0), 09.6300, 23.4000, "2023-09-25T15:45:30Z"));
        events.add(getObjectEvent(List.of(20.0, 50.0, 32.0, 100.0, 68.0), 11.6300, 34.4000, "2023-09-25T23:31:30Z"));

        ObjectTrackerDao dao = new ObjectTrackerDao();
        List<ObjectLocationHistory> locationHistory = new ObjectTracker(dao).trackObject(events);

        List<Integer> sizesList = locationHistory.stream()
                .map(history -> history.getJourneyHistory().size())
                .collect(Collectors.toList());

        assertEquals(6, locationHistory.size());
        assertEquals(3, sizesList.stream().filter( o -> o == 4).count());
        assertTrue(sizesList.contains(4));
        assertTrue(sizesList.contains(1));
        assertTrue(sizesList.contains(7));
        assertTrue(sizesList.contains(6));
    }

    private ObjectEvent getObjectEvent(List<Double> attributes, double latitude, double longitude, String timestamp) {
        TemporalCoordinate temporalCoordinate = new TemporalCoordinate(latitude, longitude, Instant.parse(timestamp).toEpochMilli());
        return new ObjectEvent(temporalCoordinate, attributes);
    }
}
