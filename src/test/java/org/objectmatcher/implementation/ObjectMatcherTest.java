package org.objectmatcher.implementation;

import org.junit.jupiter.api.Test;
import org.objectmatcher.model.ObjectAggregate;
import org.objectmatcher.model.ObjectAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectMatcherTest {

    @Test
    public void testObjectMatcher() {
        List<ObjectAttributes> testVector = new ArrayList<>();
        testVector.add( new ObjectAttributes("Id1",  List.of(1.0, 2.0, 3.0, 4.0, 5.0)));
        testVector.add(new ObjectAttributes("Id2",  List.of(1.0 , 2.0, 3.0, 4.0, 6.0)));
        testVector.add(new ObjectAttributes("Id3",  List.of(1.0 , 2.0, 3.0, 4.0, 7.0)));
        testVector.add(new ObjectAttributes("Id4",  List.of(1.0 , 2.0, 3.0, 4.0, 4.0)));



        testVector.add(new ObjectAttributes("Id5", List.of(5.0 , 6.0, 7.0, 8.0, 9.0)));
        testVector.add(new ObjectAttributes("Id6", List.of(5.0 , 6.0, 7.0, 8.0, 10.0)));
        testVector.add(new ObjectAttributes("Id7", List.of(5.0 , 6.0, 7.0, 8.0, 11.0)));
        testVector.add(new ObjectAttributes("Id8", List.of(5.0 , 6.0, 7.0, 8.0, 12.0)));
        testVector.add(new ObjectAttributes("Id9", List.of(5.0 , 6.0, 7.0, 8.0, 13.0)));

        testVector.add(new ObjectAttributes("Id10", List.of(2.0, 3.0, 4.0, 5.0, 7.0)));
        testVector.add(new ObjectAttributes("Id11", List.of(2.0, 3.0, 4.0, 5.0, 8.0)));
        testVector.add(new ObjectAttributes("Id12", List.of(2.0, 3.0, 4.0, 5.0, 9.0)));
        testVector.add(new ObjectAttributes("Id13", List.of(2.0, 3.0, 4.0, 5.0, 10.0)));

        List<ObjectAggregate> output= (new SimpleIObjectMatcherImpl(70.0)).matchObject(testVector);


        assertEquals(4, output.get(0).getObjectAttributes().size());
        assertEquals(5, output.get(1).getObjectAttributes().size());
        assertEquals(4, output.get(2).getObjectAttributes().size());


    }

//    @Test
//    public void t() {
//        List<List<Double>> groupedVectorList = List.of(
//                List.of(1.0, 2.0, 3.0),
//                List.of(4.0, 5.0, 6.0),
//                List.of(7.0, 9.0, 9.0)
//        );
//
//        List<Double> averages = IntStream.range(0, groupedVectorList.get(0).size()) // Iterate over indices
//                .mapToDouble(i -> {
//                    // Extract the i-th element from each inner list, calculate average
//                    DoubleStream values = groupedVectorList.stream().mapToDouble(innerList -> innerList.get(i));
//                    return values.average().orElse(0.0); // Use 0.0 as a default if the list is empty
//                })
//                .boxed() // Convert DoubleStream to Stream<Double>
//                .collect(Collectors.toList()); // Collect the averages into a List
//
//        System.out.println(averages); // Output: [4.0, 5.0, 6.0]
//    }

}

