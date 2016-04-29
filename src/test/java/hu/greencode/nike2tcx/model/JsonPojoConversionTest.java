package hu.greencode.nike2tcx.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.greencode.nike2tcx.model.nike.NikeActivity;
import hu.greencode.nike2tcx.model.nike.NikeActivityGpsData;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JsonPojoConversionTest {
    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testNikeActivityModel() throws IOException {
        final File nikeActivityJsonFile = new ClassPathResource("2016-03-02.json").getFile();
        final NikeActivity nikeActivity = objectMapper.readValue(nikeActivityJsonFile, NikeActivity.class);
        assertEquals(nikeActivity.getActivityId(), "1496000000019182126950000003739174189805");
        assertEquals(nikeActivity.getActivityType(), "RUN");
        final DateTime expectedStartTime = new DateTime(2016,3,2,6,41,44);
        assertEquals(nikeActivity.getStartTime(),expectedStartTime.toDate());
        assertEquals(nikeActivity.getActivityTimeZone(),"Africa/Algiers");
        assertEquals(nikeActivity.getStatus(), "COMPLETE");
        assertEquals(nikeActivity.getDeviceType(), "SPORTWATCH");
        assertEquals(nikeActivity.getMetricSummary().getCalories(), "1089");
        assertEquals(nikeActivity.getMetricSummary().getSteps(), "2008");
        assertEquals(nikeActivity.getMetrics().get(0).getValues().get(0), "12");
        assertEquals(nikeActivity.isIsGpsActivity(), true);
    }

    @Test
    public void testNikeActivityGpsDataModel() throws IOException {
        final File nikeActivityGpsDataJsonFile = new ClassPathResource("2016-03-02-gps.json").getFile();
        final NikeActivityGpsData nikeActivityGpsData =
                objectMapper.readValue(nikeActivityGpsDataJsonFile, NikeActivityGpsData.class);

        assertEquals(nikeActivityGpsData.getElevationLoss().doubleValue(), 200.96045d, 0d);
        assertEquals(nikeActivityGpsData.getElevationGain().doubleValue(), 204.73486d, 0d);
        assertEquals(nikeActivityGpsData.getElevationMax().doubleValue(), 115.05488, 0d);
        assertEquals(nikeActivityGpsData.getElevationMin().doubleValue(), 110.411964, 0d);
        assertEquals(nikeActivityGpsData.getIntervalMetric(), 10);
        assertEquals(nikeActivityGpsData.getIntervalUnit(), "SEC");
        assertEquals(nikeActivityGpsData.getWaypoints().get(0).getLatitude().doubleValue(), 47.50967d, 0d);
        assertEquals(nikeActivityGpsData.getWaypoints().get(0).getLongitude().doubleValue(), 19.11572, 0d);
        assertEquals(nikeActivityGpsData.getWaypoints().get(0).getElevation().doubleValue(), 110.4597, 0d);

    }

}
