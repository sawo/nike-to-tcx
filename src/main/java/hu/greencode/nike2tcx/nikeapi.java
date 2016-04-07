package hu.greencode.nike2tcx;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sanyi
 * Date: 07/04/16
 * Time: 14:48
 * To change this template use File | Settings | File Templates.
 */
public interface NikeApi {
    List<NikeActivity> getActivities(String activity);

    NikeActivity getActivity(String activityId);

    NikeGPSData getGPSData(String activityId);
}
