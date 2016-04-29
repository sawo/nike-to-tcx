package hu.greencode.nike2tcx.model.nike;

import java.util.List;

public class Metric {
    private int intervalMetric;
    private String intervalUnit;
    private String metricType;
    private List<String> values;

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

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
