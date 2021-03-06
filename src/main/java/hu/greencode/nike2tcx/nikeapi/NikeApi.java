package hu.greencode.nike2tcx.nikeapi;

import hu.greencode.nike2tcx.model.nike.NikeActivity;
import hu.greencode.nike2tcx.model.nike.NikeActivityGpsData;

import java.util.List;

public interface NikeApi {

    List<NikeActivity> getActivities(int offset, int pageSize);

    NikeActivity getActivity(String activityId);

    NikeActivityGpsData getGPSData(String activityId);

    void setAccessToken(String accessToken);

}
