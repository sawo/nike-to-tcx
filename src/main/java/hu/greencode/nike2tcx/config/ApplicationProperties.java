package hu.greencode.nike2tcx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class ApplicationProperties {
    private int timeout;
    private String proxyHost;
    private int proxyPort;
    private String nonProxyHosts;
    private int httpRetryAttemptCount;
    private long httpRetryDelayInMs;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getNonProxyHosts() {
        return nonProxyHosts;
    }

    public void setNonProxyHosts(String nonProxyHosts) {
        this.nonProxyHosts = nonProxyHosts;
    }

    public int getHttpRetryAttemptCount() {
        return httpRetryAttemptCount;
    }

    public void setHttpRetryAttemptCount(int httpRetryAttemptCount) {
        this.httpRetryAttemptCount = httpRetryAttemptCount;
    }

    public long getHttpRetryDelayInMs() {
        return httpRetryDelayInMs;
    }

    public void setHttpRetryDelayInMs(long httpRetryDelayInMs) {
        this.httpRetryDelayInMs = httpRetryDelayInMs;
    }
}
