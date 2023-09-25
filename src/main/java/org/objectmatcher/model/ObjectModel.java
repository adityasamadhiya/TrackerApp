package org.objectmatcher.model;

import java.util.List;

public class ObjectModel {
    private List<Double> attributes;

    String id;

    public ObjectModel(List<Double> attributes, String id) {
        this.attributes = attributes;
        this.id = id;
    }
}
