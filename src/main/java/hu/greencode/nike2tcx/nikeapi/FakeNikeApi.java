package hu.greencode.nike2tcx.nikeapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.greencode.nike2tcx.NikeApi;
import hu.greencode.nike2tcx.model.nike.NikeActivity;
import hu.greencode.nike2tcx.model.nike.NikeActivityGpsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

//@Component
public class FakeNikeApi implements NikeApi {

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setAccessToken(String accessToken) {
        // noop
    }

    @Override
    public List<NikeActivity> getActivities(int offset, int pageSize) {
        List<NikeActivity> nikeActivities = newArrayList();
        for (int i = 0; i < pageSize; i++) {
             nikeActivities.add(getActivity(null));
        }
        return nikeActivities;
    }

    @Override
    public NikeActivity getActivity(String activityId) {
        NikeActivity nikeActivity = null;
        try {
            File nikeActivityJsonFile = new ClassPathResource("2016-03-02.json").getFile();
            nikeActivity = objectMapper.readValue(nikeActivityJsonFile, NikeActivity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nikeActivity;
    }

    @Override
    public NikeActivityGpsData getGPSData(String activityId) {
        NikeActivityGpsData nikeActivityGpsData = null;
        try {
            File nikeActivityGpsDataJsonFile = new ClassPathResource("2016-03-02-gps.json").getFile();
            nikeActivityGpsData = objectMapper.readValue(nikeActivityGpsDataJsonFile, NikeActivityGpsData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nikeActivityGpsData;
    }
}
