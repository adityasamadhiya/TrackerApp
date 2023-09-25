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

//        events.add(getObjectEvent(List.of(13.0, 20.0, 15.0, 100.0, 67.0), 145.6300, 200.4000, "2023-09-25T18:31:30Z"));
//        events.add(getObjectEvent(List.of(13.0, 20.0, 16.0, 100.0, 67.0), 146.6300, 201.4000, "2023-09-25T18:41:30Z"));
//        events.add(getObjectEvent(List.of(13.0, 20.0, 15.0, 110.0, 67.0), 149.6300, 203.6000, "2023-09-25T19:11:30Z"));
//        events.add(getObjectEvent(List.of(13.0, 21.0, 15.0, 100.0, 67.0), 141.6300, 210.9000, "2023-09-25T20:31:30Z"));

//       Object 6
        ObjectTrackerDao dao = new ObjectTrackerDao();
        new ObjectTracker(dao).identifyAndTrackObject(events);
        System.out.println("Hello");
        System.out.println(dao.getAll().size());
    }

    @Test
    public void testing() {
        List<Double> a = new ArrayList<>();
        a.add(10.0);
        a.add(12.0);
        a.add(15.0);
        a.add(21.0);
        a.add(31.0);

        List<Double> b = new ArrayList<>();
        b.add(10.0);
        b.add(12.0);
        b.add(15.0);
        b.add(21.0);
        b.add(31.0);

        List<Double> c = new ArrayList<>();
        c.add(10.0);
        c.add(12.0);
        c.add(15.0);
        c.add(21.0);
        c.add(31.0);

        System.out.println(a.hashCode());
        System.out.println(b.hashCode());
        System.out.println(c.hashCode());
        System.out.println(a.equals(b));
    }
    private ObjectEvent getObjectEvent(List<Double> attributes, double latitude, double longitude, String timestamp) {
        TemporalCoordinate temporalCoordinate = new TemporalCoordinate(latitude, longitude, Instant.parse(timestamp).toEpochMilli());
        return new ObjectEvent(temporalCoordinate, attributes);
    }
}
