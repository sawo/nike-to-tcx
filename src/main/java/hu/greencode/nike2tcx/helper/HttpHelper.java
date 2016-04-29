package hu.greencode.nike2tcx.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.greencode.nike2tcx.config.ApplicationProperties;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class HttpHelper {

    private static Logger logger = LoggerFactory.getLogger(HttpHelper.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ApplicationProperties applicationProperties;

    private HttpClient httpClient;

    @PostConstruct
    public void init() {
        logger.info("Application properties: [{}]", applicationProperties);
        httpClient = httpClientInstance();
    }

    public <T> T doPostJson(String url, Map<String, Object> requestBodyMap, Class<T> returnType) throws Exception {
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(objectMapper.writeValueAsString(requestBodyMap), "utf-8"));
        return generateResponse(post, returnType, url);
    }

    private <T> T generateResponse(HttpRequestBase method, Class<T> returnType, String url) throws Exception {
        for (int i = 0; i < applicationProperties.getHttpRetryAttemptCount(); i++) {
            try {
                final HttpResponse httpResponse = httpClient.execute(method);
                if (returnType == null) {
                    return null;
                }
                logger.debug("HTTP [{}] call with the following url: [{}], returned with HTTP Code [{}].",
                        method.getMethod(), url, httpResponse.getStatusLine().getStatusCode());
                return objectMapper.readValue(httpResponse.getEntity().getContent(), returnType);
            } catch (Exception e) {
                // NOOP
            }
            Thread.sleep(applicationProperties.getHttpRetryDelayInMs());
        }
        logger.error("Unable to connect to the following url [{}] via [{}] method. Sorry, i gave up after {} times " +
                "of trial", url, method.getMethod(), applicationProperties.getHttpRetryAttemptCount());
        throw new Exception("Unable to connect to the url " + url);
    }

    private HttpClient httpClientInstance() {
        HttpClient client;
        int timeout = applicationProperties.getTimeout() * 1000;
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
        if (isProxyEnabled()) {
            HttpHost proxy = new HttpHost(applicationProperties.getProxyHost(), applicationProperties.getProxyPort());
            HttpRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy) {

                @Override
                public HttpRoute determineRoute(final HttpHost host,
                                                final HttpRequest request,
                                                final HttpContext context) throws HttpException {
                    String hostname = host.getHostName();
                    final List<String> nonProxyHosts = Arrays.asList(applicationProperties.getNonProxyHosts());
                    if (nonProxyHosts.contains(hostname)) {
                        logger.debug("HttpClient will SKIP proxy, because [{}] is on the this []",
                                hostname,
                                nonProxyHosts.toArray().toString());
                        return new HttpRoute(host);
                    }
                    logger.debug("HttpClient will USE proxy, because [{}] is on the this []",
                            hostname,
                            nonProxyHosts.toArray().toString());
                    return super.determineRoute(host, request, context);
                }
            };
            client = HttpClients.custom().setRoutePlanner(routePlanner).setDefaultRequestConfig(requestConfig).build();
        } else {
            client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        }
        logger.debug("HttpClient was created. Proxy: [{}]", isProxyEnabled());
        return client;
    }

    private boolean isProxyEnabled() {
        final String proxyHost = applicationProperties.getProxyHost();
        boolean proxyAvailable = proxyHost != null && proxyHost.length() > 0;
        return proxyAvailable;
    }

    public <T> T doGetJson(String url, Class<T> returnType) throws Exception {
        HttpGet get = new HttpGet(url);
        get.addHeader("Content-Type", "application/json");
        return generateResponse(get, returnType, url);
    }

    public HttpResponse doGet(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        return httpClient.execute(get);
    }

}