package hu.greencode.nike2tcx.service;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import hu.greencode.nike2tcx.model.nike.Metric;
import hu.greencode.nike2tcx.model.nike.NikeActivity;
import hu.greencode.nike2tcx.model.tcx.Lap;
import hu.greencode.nike2tcx.model.tcx.TrackPoint;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class NikeActivityReader {

    // intentionally left as null
    private List<TrackPoint> trackPoints;

    private List<Duration> lapDurations = newArrayList();
    private List<DateTime> lapStartTimes = newArrayList();

    private List<Lap> laps = newArrayList();

    private NikeActivity nikeActivity;

    public NikeActivityReader(NikeActivity nikeActivity) {
        this.nikeActivity = nikeActivity;
    }

    public void convert() throws IOException {
        process();
        readLapData();
        adjustHeartRate();
        generateXml();
    }

    private void readLapData() throws IOException {
        System.out.print("Number of laps: ");
        byte[] numberOfLapsByteArray = new byte[40];
        System.in.read(numberOfLapsByteArray);
        int numberOfLaps = Integer.parseInt(new String(numberOfLapsByteArray).trim());

        if (numberOfLaps < 1) {
            throw new IOException("Number of laps cannot be less than 1: " + numberOfLaps);
        }

        if (numberOfLaps > 1) {
            for (int i = 0; i < numberOfLaps; i++) {
                byte[] lapTimeByteArray = new byte[40];
                System.out.print("What is the " + (i + 1) + ". lap's duration? [mm:ss] : ");
                System.in.read(lapTimeByteArray);
                PeriodFormatter formatter = new PeriodFormatterBuilder()
                                                .appendMinutes()
                                                .appendLiteral(":")
                                                .appendSeconds()
                                                .toFormatter();
                final Duration lapDuration = formatter.parsePeriod(new String(lapTimeByteArray).trim()).toStandardDuration();
                lapDurations.add(lapDuration);
            }
        }
        if (lapDurations.size() == 0) {
            final String durationString = nikeActivity.getMetricSummary().getDuration();
            PeriodFormatter formatter = new PeriodFormatterBuilder()
                                            .appendHours()
                                            .appendLiteral(":")
                                            .appendMinutes()
                                            .appendLiteral(":")
                                            .appendSeconds()
                                            .appendLiteral(".")
                                            .appendMillis3Digit()
                                            .toFormatter();

            lapDurations.add(formatter.parsePeriod(durationString).toStandardDuration());
        }

        lapStartTimes = convertLapDurationsToLapStartTimes(lapDurations);
        // one lap only ...
        if (lapStartTimes.size() == 2) {
            Lap lap = new Lap();
            lap.setTrackPoints(trackPoints);
            laps.add(lap);
        }

        // multiple laps ...
        int
            lastUsedTrackPointIndex = 0;
        for (int lapStartTimeIndex = 0; lapStartTimeIndex < lapStartTimes.size() - 1; lapStartTimeIndex++) {
            DateTime lapStartTime = lapStartTimes.get(lapStartTimeIndex);
            DateTime nextLapStartTime = lapStartTimes.get(lapStartTimeIndex + 1);
            Lap lap = new Lap();
            List<TrackPoint> trackPointsOfLap = newArrayList();
            for (int trackPointIntex = 0; trackPointIntex < trackPoints.size(); trackPointIntex++) {
                  TrackPoint trackPoint = trackPoints.get(lapStartTimeIndex);
                if ( ( trackPoint.getTime().isAfter(lapStartTime) || trackPoint.getTime().isEqual(lapStartTime)) &&
                     ( trackPoint.getTime().isBefore(nextLapStartTime) || trackPoint.getTime().isEqual(nextLapStartTime) ) ) {
                    trackPointsOfLap.add(trackPoint);
                    lastUsedTrackPointIndex = trackPointIntex;
                }
            }
            lap.setTrackPoints(trackPointsOfLap);
            laps.add(lap);
        }
        System.out.println("lap size: " + laps.size());

    }

    private List<DateTime> convertLapDurationsToLapStartTimes(List<Duration> lapDurations) {
        List<DateTime> lapStartTimes = newArrayList();
        DateTime activityStartTime = new DateTime(nikeActivity.getStartTime());
        lapStartTimes.add(activityStartTime);

        for (int i = 0; i < lapDurations.size(); i++) {
            DateTime previousLapStartTime = lapStartTimes.get(i);
            Duration duration = lapDurations.get(i);
            lapStartTimes.add(previousLapStartTime.plusMillis((int) duration.getMillis()));
        }
        return lapStartTimes;
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
        String activityStartTime = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").print(new DateTime(nikeActivity.getStartTime()));
        result.append("<Id>" + activityStartTime + "</Id>\n");
        for (Lap lap : laps) {
            String lapStartTime = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").print(lap.getStartTime());
            result.append("<Lap StartTime=\"" + lapStartTime + "\">\n");
            result.append("<Track>\n");
            for (TrackPoint trackPoint : lap.getTrackPoints()) {
                result.append(trackPoint.toXML());
            }
            result.append("</Track>\n");
            result.append("</Lap>\n");
        }
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
