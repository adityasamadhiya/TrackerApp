package org.objectmatcher;

import org.objectmatcher.model.ObjectAggregate;
import org.objectmatcher.model.ObjectAttributes;

import java.util.List;

public interface ObjectMatcherInterface {
    public List<ObjectAggregate> matchObject(List<ObjectAttributes> attributeVector);
}
