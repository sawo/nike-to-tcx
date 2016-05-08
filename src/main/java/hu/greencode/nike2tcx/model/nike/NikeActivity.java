package hu.greencode.nike2tcx.model.nike;

import java.util.Date;
import java.util.List;

public class NikeActivity {
    private List<Link> links;
    private String activityId;
    private String activityType;
    private Date startTime;
    private String activityTimeZone;
    private String status;
    private String deviceType;
    private MetricSummary metricSummary;
    private List<Tag> tags;
    private List<Metric> metrics;
    private boolean isGpsActivity;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getActivityTimeZone() {
        return activityTimeZone;
    }

    public void setActivityTimeZone(String activityTimeZone) {
        this.activityTimeZone = activityTimeZone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public MetricSummary getMetricSummary() {
        return metricSummary;
    }

    public void setMetricSummary(MetricSummary metricSummary) {
        this.metricSummary = metricSummary;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public boolean isIsGpsActivity() {
        return isGpsActivity;
    }

    public void setIsGpsActivity(boolean gpsActivity) {
        isGpsActivity = gpsActivity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NikeActivity{");
        sb.append("activityId='").append(activityId).append('\'');
        sb.append(", activityType='").append(activityType).append('\'');
        sb.append(", startTime=").append(startTime);
        sb.append(", status='").append(status).append('\'');
        sb.append(", deviceType='").append(deviceType).append('\'');
        sb.append(", tags=").append(tags);
        sb.append(", isGpsActivity=").append(isGpsActivity);
        sb.append('}');
        return sb.toString();
    }
}
