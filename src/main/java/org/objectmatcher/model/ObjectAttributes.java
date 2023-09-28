package org.objectmatcher.model;

import java.util.List;

public class ObjectAttributes {
    private List<Double> attributes;

    private String id;

    public ObjectAttributes(String id, List<Double> attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    public List<Double> getAttributes() {
        return List.copyOf(attributes);
    }

    public String getId() {
        return id;
    }
}
