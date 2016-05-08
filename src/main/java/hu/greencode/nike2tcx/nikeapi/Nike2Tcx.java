package hu.greencode.nike2tcx.nikeapi;

import hu.greencode.nike2tcx.NikeActivityReader;
import hu.greencode.nike2tcx.NikeApi;
import hu.greencode.nike2tcx.model.nike.NikeActivity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class Nike2Tcx implements CommandLineRunner {

    @Autowired
    private NikeApi nikeApi;

    public static void main(String[] args)  {
        new SpringApplicationBuilder().web(false).sources(Nike2Tcx.class).showBanner(false).run(args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.print("What is your access token? ");
        byte[] accessTokenByteArray = new byte[40];
        System.in.read(accessTokenByteArray);
        final String accessToken = new String(accessTokenByteArray).trim();
        nikeApi.setAccessToken(accessToken);
        System.out.println("Retrieving the most recent runs ...");
        List<NikeActivity> activities = nikeApi.getActivities(1, 10);
        for (int i = 0; i < activities.size(); i++) {
            System.out.println(i + ". " + activities.get(i));
        }
        int selectedActivityPosition = getSelectedActivityFromUser(activities.size());
        NikeActivity selectedActivity = nikeApi.getActivity(activities.get(selectedActivityPosition).getActivityId());
        new NikeActivityReader(selectedActivity).convert();
    }

    private int getSelectedActivityFromUser(int numberOfActivities) throws IOException {
        byte[] selectedActivityPositionAsByte = new byte[2];
        int selectedActivityPosition;
        do {
            System.out.print("Which activity do you want to download? [0-" + (numberOfActivities - 1) + "] ");
            System.in.read(selectedActivityPositionAsByte);
            String selectedActivityPositionAsString = new String(selectedActivityPositionAsByte).trim();
            selectedActivityPosition = Integer.parseInt(selectedActivityPositionAsString);
        } while (selectedActivityPosition < 0 || selectedActivityPosition >= numberOfActivities);
        return selectedActivityPosition;
    }
}
