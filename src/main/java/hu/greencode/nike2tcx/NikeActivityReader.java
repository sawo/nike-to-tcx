package hu.greencode.nike2tcx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import hu.greencode.nike2tcx.model.tcx.TrackPoint;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class NikeActivityReader {

    private Map<String, Object> activityMap;

    private DateTime startTime;

    private List<TrackPoint> trackPoints;

    private final String activityFileName;

    private ObjectMapper objectMapper = new ObjectMapper();

    public NikeActivityReader(String activityFileName) {
        this.activityFileName = activityFileName;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public List<TrackPoint> getTrackPoints() {
        return trackPoints;
    }

    public void setTrackPoints(List<TrackPoint> trackPoints) {
        this.trackPoints = trackPoints;
    }

    public void convert() throws IOException {
        readJSONs();
        process();
        print();
        adjustHeartRate();
        print();
        generateXml();
    }

    private void generateXml() throws IOException {
        StringBuilder result;
        result = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n" +
                "<TrainingCenterDatabase xmlns=\"http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2\"\n" +
                "                        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "                        xsi:schemaLocation=\"http://www.garmin.com/xmlschemas/ActivityExtension/v2 http://www.garmin.com/xmlschemas/ActivityExtensionv2.xsd http://www.garmin.com/xmlschemas/FatCalories/v1 http://www.garmin.com/xmlschemas/fatcalorieextensionv1.xsd http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd\">\n" +
                "    <Folders/>");
        result.append("<Activities>\n");
        result.append("<Activity Sport=\"Running\">\n");
        String date =  DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").print(startTime);
        result.append("<Id>" + date + "</Id>\n");
        result.append("<Lap StartTime=\"" + date + "\">\n");
        String durationString = (String) ((Map) activityMap.get("metricSummary")).get("duration");
        String caloriesString = (String) ((Map) activityMap.get("metricSummary")).get("calories");
        DateTimeFormatter dateStringFormat = DateTimeFormat.forPattern("HH:mm:ss.SSS");
        DateTime duration = dateStringFormat.parseDateTime(durationString);
        result.append("<TotalTimeSeconds>" + duration.getSecondOfDay() + "</TotalTimeSeconds>\n");
        Double distanceKiloMeter = Double.parseDouble((String) ((Map) activityMap.get("metricSummary")).get("distance"));
        Double distanceMeter = distanceKiloMeter * 1000;
        result.append("<DistanceMeters>" + distanceMeter + "</DistanceMeters>\n");
        result.append("<MaximumSpeed>0</MaximumSpeed>\n");
        result.append("<Calories>" + caloriesString + "</Calories>\n");
        result.append("<Intensity>Resting</Intensity>\n");
        result.append("<Cadence>0</Cadence>\n");
        result.append("<TriggerMethod>Manual</TriggerMethod>\n");
        result.append("<Track>\n");
        for (TrackPoint trackPoint : trackPoints) {
            result.append(trackPoint.toXML());
        }
        result.append("</Track>\n");
        result.append("</Lap>\n");
        result.append("</Activity>\n");
        result.append("</Activities>\n");
        result.append("</TrainingCenterDatabase>");
        Files.write(result.toString(), new File(activityFileName + ".tcx"), Charsets.UTF_8);
    }

    private void adjustHeartRate() {

        if (trackPoints != null) {
            for (int i = 0; i < trackPoints.size(); i++) {
                TrackPoint trackPoint = trackPoints.get(i);
                if (trackPoint.getHeartRate().intValue() == 0) {
                    final boolean isFirstPoint = i == 0;
                    final boolean isLastPoint = i == trackPoints.size() - 1;
                    final int nextHeartRatePosition = getNextValidHeartRatePosition(i);

                    if (!isFirstPoint && !isLastPoint && nextHeartRatePosition != -1) {
                        int beatsMissing = nextHeartRatePosition - i;
                        int previousValidHeartRate = trackPoints.get(i - 1).getHeartRate();
                        int heartRateGap = trackPoints.get(nextHeartRatePosition).getHeartRate() -
                                previousValidHeartRate;
                        double lift = ((double) heartRateGap) / beatsMissing;
                        for (int j = 0; j < beatsMissing; j++) {
                            trackPoints.get(i + j).setHeartRate((int) Math.round(previousValidHeartRate + (j + 1) * lift));
                        }
                    }
                }

            }
        }
    }

    private int getNextValidHeartRatePosition(int i) {
        for (int j = i; j < trackPoints.size(); j++) {
            if (trackPoints.get(j).getHeartRate() != 0) {
                return j;
            }
        }
        return -1;
    }

    private void readJSONs() throws IOException {
        activityMap = objectMapper.readValue(new File(activityFileName), Map.class);
    }

    private void process() {
        startTime = new DateTime(activityMap.get("startTime"));
        final List<Map> metrics = (List<Map>) activityMap.get("metrics");
        for (Map metric : metrics) {
            if (metric.get("metricType").equals("FUEL")) {
                continue;
            }
            List<Object> values = (List<Object>) metric.get("values");
            for (int i = 0; i < values.size(); i++) {
                if (trackPoints == null) {
                    initTrackPointList(startTime, 10, values.size());
                }
            }
            for (int i = 0; i < values.size(); i++) {
                if (metric.get("metricType").equals("HEARTRATE")) {
                    trackPoints.get(i).setHeartRate(Integer.parseInt((String) values.get(i)));
                }
                if (metric.get("metricType").equals("DISTANCE")) {
                    trackPoints.get(i).setDistanceMeters(1000 * Double.parseDouble((String) values.get(i)));
                }

            }
        }
    }

    private void initTrackPointList(DateTime startTime, int intervalInSeconds, int size) {
        trackPoints = newArrayList();
        for (int i = 0; i < size; i++) {
            TrackPoint tp = new TrackPoint();
            tp.setTime(startTime.plusSeconds(i * intervalInSeconds));
            tp.setPosition(i);
            trackPoints.add(tp);
        }

    }

    private void print() {
        for (TrackPoint trackPoint : trackPoints) {
            System.out.println(trackPoint);
        }
    }

}
