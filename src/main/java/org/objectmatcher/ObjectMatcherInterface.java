package org.objectmatcher;

import org.objectmatcher.model.ObjectAggregate;
import org.objectmatcher.model.ObjectModel;

import java.util.List;

public interface ObjectMatcherInterface {
    public List<ObjectAggregate> matchObject(List<ObjectModel> attributeVector, Double threshold);
}
