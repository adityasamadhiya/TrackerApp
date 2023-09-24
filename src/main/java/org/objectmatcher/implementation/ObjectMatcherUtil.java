package org.objectmatcher.implementation;

import org.objectmatcher.ObjectMatcherInterface;
import org.objectmatcher.model.ObjectAggregate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ObjectMatcherUtil implements ObjectMatcherInterface {

    public List<ObjectAggregate> matchObject(List<List<Double>> attributeVector, Double threshold) {
        Set<List<Double>> matchedVectorsSet = new HashSet<>();
        List<ObjectAggregate> aggregateGroupedObjects = new ArrayList<>();
        attributeVector.forEach(v1-> {

            if(matchedVectorsSet.contains(v1))
                return;

            List<List<Double>> groupedVectorList = new ArrayList<>();

            groupedVectorList.add(v1);
            matchedVectorsSet.add(v1);


            attributeVector.forEach( v2 -> {
                if(matchedVectorsSet.contains(v2))
                    return;
                if(compare(v1,v2, threshold)) {
                    groupedVectorList.add(v2);
                    matchedVectorsSet.add(v2);
                }
            });

            aggregateGroupedObjects.add(createObjectAggregate(groupedVectorList));
        });
        return aggregateGroupedObjects;
    }

    private static ObjectAggregate createObjectAggregate(List<List<Double>> groupedVectorList) {
        List<Double> aggregate = computeAggregate(groupedVectorList);
        return new ObjectAggregate(groupedVectorList, aggregate);
    }

    private static List<Double> computeAggregate(List<List<Double>> groupedVectorList) {
        List<Double> aggregate = new ArrayList<>();
        for(int index=0; index < groupedVectorList.get(0).size(); index++) {
            aggregate.add(getElementAverage(groupedVectorList, index));
        }
        return aggregate;
    }

    private static Double getElementAverage(List<List<Double>> groupedVectorList, int index) {
        Integer count = groupedVectorList.size();
        if(count == 0)
            return 0.0;
        Double sum = groupedVectorList.stream().mapToDouble(innerList -> innerList.get(index)).sum();
        return sum/count;
    }

    protected static Boolean compare(List<Double> v1, List<Double> v2, Double threshold) {
        if(v1.size() != v2.size())
            return false;
        int size = v1.size();
        int matchCount = 0;

        for(int index = 0; index < v1.size(); index++) {
            if(v1.get(index).equals(v2.get(index)))
                matchCount++;
        }
        return Double.compare(matchCount * 100.00/size, threshold) == 1 ? Boolean.TRUE : Boolean.FALSE;

    }
}
