package hu.greencode.nike2tcx.service;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import hu.greencode.nike2tcx.model.nike.Metric;
import hu.greencode.nike2tcx.model.nike.NikeActivity;
import hu.greencode.nike2tcx.model.tcx.TrackPoint;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class NikeActivityReader {

    private List<TrackPoint> trackPoints;

    private NikeActivity nikeActivity;

    public NikeActivityReader(NikeActivity nikeActivity) {
        this.nikeActivity = nikeActivity;
    }

    public void convert() throws IOException {
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
        String startDate = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").print(new DateTime(nikeActivity.getStartTime()));
        result.append("<Id>" + startDate + "</Id>\n");
        result.append("<Lap StartTime=\"" + startDate + "\">\n");
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss.SSS");
        DateTime duration = dateTimeFormatter.parseDateTime(nikeActivity.getMetricSummary().getDuration());
        result.append("<TotalTimeSeconds>" + duration.getSecondOfDay() + "</TotalTimeSeconds>\n");
        Double distanceKiloMeter = Double.parseDouble(nikeActivity.getMetricSummary().getDistance());
        Double distanceMeter = distanceKiloMeter * 1000;
        result.append("<DistanceMeters>" + distanceMeter + "</DistanceMeters>\n");
        result.append("<MaximumSpeed>0</MaximumSpeed>\n");
        result.append("<Calories>" + nikeActivity.getMetricSummary().getCalories() + "</Calories>\n");
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
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-M-dd");
        final String tcxFileName = dateFormatter.format(nikeActivity.getStartTime()) + "-" +
                nikeActivity.getActivityId() + ".tcx";
        Files.write(result.toString(), new File(tcxFileName), Charsets.UTF_8);
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

    private void process() {
        for (Metric metric : nikeActivity.getMetrics()) {

            if (metric.getMetricType().equals("FUEL")) {
                continue;
            }

            for (int i = 0; i < metric.getValues().size(); i++) {
                if (trackPoints == null) {
                    initTrackPointList(new DateTime(nikeActivity.getStartTime()), 10, metric.getValues().size());
                }
            }
            for (int i = 0; i < metric.getValues().size(); i++) {
                if (metric.getMetricType().equals("HEARTRATE")) {
                    trackPoints.get(i).setHeartRate(Integer.parseInt(metric.getValues().get(i)));
                }
                if (metric.getMetricType().equals("DISTANCE")) {
                    trackPoints.get(i).setDistanceMeters(1000 * Double.parseDouble(metric.getValues().get(i)));
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
