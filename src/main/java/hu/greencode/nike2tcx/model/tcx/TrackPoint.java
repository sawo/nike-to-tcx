package hu.greencode.nike2tcx.model.tcx;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class TrackPoint {

    private int position;
    private DateTime time;
    private Double latitudeDegrees = 0d;
    private Double longitudeDegrees = 0d;
    private Double altitudeMeters;
    private Double distanceMeters;
    private Integer heartRate;

    public TrackPoint() {

    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
    }

    public Double getLatitudeDegrees() {
        return latitudeDegrees;
    }

    public void setLatitudeDegrees(Double latitudeDegrees) {
        this.latitudeDegrees = latitudeDegrees;
    }

    public Double getLongitudeDegrees() {
        return longitudeDegrees;
    }

    public void setLongitudeDegrees(Double longitudeDegrees) {
        this.longitudeDegrees = longitudeDegrees;
    }

    public Double getAltitudeMeters() {
        return altitudeMeters;
    }

    public void setAltitudeMeters(Double altitudeMeters) {
        this.altitudeMeters = altitudeMeters;
    }

    public Double getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(Double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TrackPoint{");
        sb.append("position=").append(position);
        sb.append(", time=").append(time);
        sb.append(", latitudeDegrees=").append(latitudeDegrees);
        sb.append(", longitudeDegrees=").append(longitudeDegrees);
        sb.append(", altitudeMeters=").append(altitudeMeters);
        sb.append(", distanceMeters=").append(distanceMeters);
        sb.append(", heartRate=").append(heartRate);
        sb.append('}');
        return sb.toString();
    }

    public String toXML() {
        final StringBuilder sb = new StringBuilder("<Trackpoint>\n");
        sb.append("<Time>");
        if (time != null) {
            sb.append(DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").print(time));
        }
        sb.append("</Time>\n");
        sb.append("<Position>\n");
        sb.append("<LatitudeDegrees>");
        sb.append(0);
        sb.append("</LatitudeDegrees>\n");
        sb.append("<LongitudeDegrees>");
        sb.append(0);
        sb.append("</LongitudeDegrees>\n");
        sb.append("</Position>\n");
        sb.append("<AltitudeMeters>");
        sb.append(0);
        sb.append("</AltitudeMeters>\n");
        sb.append("<DistanceMeters>");
        sb.append(distanceMeters);
        sb.append("</DistanceMeters>\n");
        sb.append("<HeartRateBpm xsi:type=\"HeartRateInBeatsPerMinute_t\">\n");
        sb.append("<Value>");
        sb.append(heartRate);
        sb.append("</Value>\n");
        sb.append("</HeartRateBpm>\n");
        sb.append("<Extensions></Extensions>\n");
        sb.append("</Trackpoint>\n");
        return sb.toString();
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
