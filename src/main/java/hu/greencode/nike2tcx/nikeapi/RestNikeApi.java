package hu.greencode.nike2tcx.nikeapi;

import hu.greencode.nike2tcx.model.nike.NikeActivities;
import hu.greencode.nike2tcx.model.nike.NikeActivity;
import hu.greencode.nike2tcx.model.nike.NikeActivityGpsData;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class RestNikeApi implements NikeApi {

    private RestTemplate restTemplate = new RestTemplate();

    private String accessToken;

    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public List<NikeActivity> getActivities(int offset, int pageSize) {
        StringBuilder url = new StringBuilder("https://api.nike.com/v1/me/sport/activities");
        url.append("?access_token=");
        url.append(accessToken);
        url.append("&count=");
        url.append(pageSize);
        url.append("&offset=");
        url.append(offset);
        NikeActivities activities = restTemplate.getForObject(url.toString(), NikeActivities.class);
        return activities.getData();
    }

    @Override
    public NikeActivity getActivity(String activityId) {
        StringBuilder url = new StringBuilder("https://api.nike.com/v1/me/sport/activities");
        url.append("/");
        url.append(activityId);
        url.append("?access_token=");
        url.append(accessToken);
        NikeActivity activity = restTemplate.getForObject(url.toString(), NikeActivity.class);
        return activity;
    }

    @Override
    public NikeActivityGpsData getGPSData(String activityId) {
        StringBuilder url = new StringBuilder("https://api.nike.com/v1/me/sport/activities");
        url.append("/");
        url.append(activityId);
        url.append("/gps");
        url.append("?access_token=");
        url.append(accessToken);
        NikeActivityGpsData gpsData = restTemplate.getForObject(url.toString(), NikeActivityGpsData.class);
        return gpsData;
    }
}
