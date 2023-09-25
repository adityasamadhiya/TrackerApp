package org.objectmatcher.model;

import java.util.List;
import java.util.UUID;

public class ObjectAggregate {

    private List<ObjectModel> objectAttributes;
    private List<Double> objectAggregateAttributes;

    public ObjectAggregate(List<ObjectModel> objectAttributes, List<Double> objectAggregateAttributes) {
        this.objectAttributes = objectAttributes;
        this.objectAggregateAttributes = objectAggregateAttributes;
    }

    public List<ObjectModel> getObjectAttributes() {
        return objectAttributes;
    }

    public List<Double> getObjectAggregateAttributes() {
        return List.copyOf(objectAggregateAttributes);
    }

}
