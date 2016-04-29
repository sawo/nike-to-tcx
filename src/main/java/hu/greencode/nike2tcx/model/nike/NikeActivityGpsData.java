package hu.greencode.nike2tcx.model.nike;

import java.util.List;

public class NikeActivityGpsData {
    private List<Link> links;
    private Double elevationLoss;
    private Double elevationGain;
    private Double elevationMax;
    private Double elevationMin;
    private int intervalMetric;
    private String intervalUnit;
    private List<Waypoint> waypoints;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public Double getElevationLoss() {
        return elevationLoss;
    }

    public void setElevationLoss(Double elevationLoss) {
        this.elevationLoss = elevationLoss;
    }

    public Double getElevationGain() {
        return elevationGain;
    }

    public void setElevationGain(Double elevationGain) {
        this.elevationGain = elevationGain;
    }

    public Double getElevationMax() {
        return elevationMax;
    }

    public void setElevationMax(Double elevationMax) {
        this.elevationMax = elevationMax;
    }

    public Double getElevationMin() {
        return elevationMin;
    }

    public void setElevationMin(Double elevationMin) {
        this.elevationMin = elevationMin;
    }

    public int getIntervalMetric() {
        return intervalMetric;
    }

    public void setIntervalMetric(int intervalMetric) {
        this.intervalMetric = intervalMetric;
    }

    public String getIntervalUnit() {
        return intervalUnit;
    }

    public void setIntervalUnit(String intervalUnit) {
        this.intervalUnit = intervalUnit;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }
}