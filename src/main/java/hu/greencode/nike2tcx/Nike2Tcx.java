package hu.greencode.nike2tcx;

import hu.greencode.nike2tcx.model.nike.NikeActivity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@SpringBootApplication
public class Nike2Tcx implements CommandLineRunner {

    private NikeApi nikeApi = new FakeNikeApi();

    public static void main(String[] args)  {
        new SpringApplicationBuilder().web(false).sources(Nike2Tcx.class).showBanner(false).run(args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.print("What is your access token? ");
        byte[] accessTokenByteArray = new byte[40];
        System.in.read(accessTokenByteArray);
        final String accessToken = new String(accessTokenByteArray);
        System.out.println("Retrieving the most recent runs ...");
        List<NikeActivity> activities = nikeApi.getActivities(0, 10);
        new NikeActivityReader(strings[0]).convert();
    }
}
