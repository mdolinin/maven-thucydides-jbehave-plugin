package net.thucydides.maven.plugin;

import net.thucydides.maven.plugin.utils.DeepEqualsWithExclusion;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class WhenUseDeepEqualsWithExclusionTest {

    private List<String> listToExclude = new ArrayList<String>();
    private Map<String, String> actualData = new LinkedHashMap<String, String>();
    private Map<String, String> expectedData = new LinkedHashMap<String, String>();

    @Test
    public void compareToSimpleObjects() {
        DeepEqualsWithExclusion equal = new DeepEqualsWithExclusion(listToExclude, expectedData, actualData);
        Simple expectedResponse = new Simple();
        expectedResponse.setSimpleId("1");
        Simple actualResponse = new Simple();
        actualResponse.setSimpleId("1");
        equal.deepEqual(null, expectedResponse, actualResponse);
        assertTrue("Object are different - expected: " + expectedData + " \n but was: " + actualData, (expectedData.isEmpty() && actualData.isEmpty()));
    }

    @Test
    public void compareToObjectsWithLists() {
        DeepEqualsWithExclusion equal = new DeepEqualsWithExclusion(listToExclude, expectedData, actualData);

        Simple simple1 = new Simple();
        simple1.setSimpleId("1");

        Simple simple2 = new Simple();
        simple2.setSimpleId("2");

        Simple simple3 = new Simple();
        simple3.setSimpleId("3");

        SimpleWithList expectedResponse = new SimpleWithList();
        expectedResponse.getSimpleList().add(simple1);
        expectedResponse.getSimpleList().add(simple2);
        expectedResponse.getSimpleList().add(simple3);

        SimpleWithList actualResponse = new SimpleWithList();
        actualResponse.getSimpleList().add(simple1);
        actualResponse.getSimpleList().add(simple3);
        actualResponse.getSimpleList().add(simple2);

        equal.deepEqual(null, expectedResponse, actualResponse);
        assertTrue("Object are different - expected: " + expectedData + " \n but was: " + actualData, (expectedData.isEmpty() && actualData.isEmpty()));
    }
}
