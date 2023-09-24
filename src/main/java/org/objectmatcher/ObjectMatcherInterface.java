package org.objectmatcher;

import org.objectmatcher.model.ObjectAggregate;

import java.util.List;

public interface ObjectMatcherInterface {
    public List<ObjectAggregate> matchObject(List<List<Double>> attributeVector, Double threshold);
}
