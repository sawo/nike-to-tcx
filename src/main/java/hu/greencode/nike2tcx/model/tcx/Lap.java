package hu.greencode.nike2tcx.model.tcx;

import org.joda.time.DateTime;

import java.util.List;

public class Lap {

    private List<TrackPoint> trackPoints;

    public DateTime getStartTime() {
        if (trackPoints != null && trackPoints.size() > 0 ) {
            return trackPoints.get(0).getTime();
        } else {
            return null;
        }
    }
    public DateTime getEndTime() {
        if (trackPoints != null && trackPoints.size() > 0 ) {
            return trackPoints.get(trackPoints.size() - 1).getTime();
        } else {
            return null;
        }
    }

    public List<TrackPoint> getTrackPoints() {
        return trackPoints;
    }

    public void setTrackPoints(List<TrackPoint> trackPoints) {
        this.trackPoints = trackPoints;
    }
}
