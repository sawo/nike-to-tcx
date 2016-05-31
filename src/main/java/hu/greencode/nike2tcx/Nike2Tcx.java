package hu.greencode.nike2tcx;

import hu.greencode.nike2tcx.model.nike.NikeActivity;
import hu.greencode.nike2tcx.nikeapi.NikeApi;
import hu.greencode.nike2tcx.service.NikeActivityReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@SpringBootApplication
public class Nike2Tcx implements CommandLineRunner {

    @Autowired
    private NikeApi nikeApi;

    public static void main(String[] args) {
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

        while (true) {
            final Iterator<NikeActivity> iterator = activities.iterator();
            while (iterator.hasNext()) {
                NikeActivity activity = iterator.next();
                if (!activity.getActivityType().equals("RUN")) {
                    iterator.remove();
                }
            }
            System.out.println("\nSelect an activity from the list below: ");
            for (int i = 0; i < activities.size(); i++) {
                System.out.println(i + ". " + activities.get(i));
            }
            int selectedActivityPosition;
            try {
                selectedActivityPosition = getSelectedActivityFromUser(activities.size());
            } catch (NumberFormatException e) {
                System.out.println("Thank you for using. Have a nice running!");
                break;
            }
            NikeActivity selectedActivity = nikeApi.getActivity(activities.get(selectedActivityPosition).getActivityId());
            new NikeActivityReader(selectedActivity).convert();
            activities.remove(selectedActivityPosition);
        }
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
