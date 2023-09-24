package org.objectmatcher.model;

import java.util.List;
import java.util.UUID;

public class ObjectAggregate {

    private List<List<Double>> objectAttributes;
    private List<Double> objectAggregateAttributes;

    public ObjectAggregate(List<List<Double>> objectAttributes, List<Double> objectAggregateAttributes) {
        this.objectAttributes = objectAttributes;
        this.objectAggregateAttributes = objectAggregateAttributes;
    }

    public List<List<Double>> getObjectAttributes() {
        return List.copyOf(objectAttributes);
    }

    public List<Double> getObjectAggregateAttributes() {
        return List.copyOf(objectAggregateAttributes);
    }

}
