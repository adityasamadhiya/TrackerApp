package org.tracker;

import org.tracker.model.ObjectEvent;
import org.tracker.model.ObjectLocationHistory;

import java.util.List;

public interface IObjectTracker {
    public List<ObjectLocationHistory> trackObjects(List<ObjectEvent> objectEvents);
}
