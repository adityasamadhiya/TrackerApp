package org.objectmatcher.model;

import java.util.List;

public class ObjectAggregate {

    private List<ObjectAttributes> objectAttributes;
    private List<Double> objectAggregateAttributes;

    public ObjectAggregate(List<ObjectAttributes> objectAttributes, List<Double> objectAggregateAttributes) {
        this.objectAttributes = objectAttributes;
        this.objectAggregateAttributes = objectAggregateAttributes;
    }

    public List<ObjectAttributes> getObjectAttributes() {
        return objectAttributes;
    }

    public List<Double> getObjectAggregateAttributes() {
        return List.copyOf(objectAggregateAttributes);
    }

}
