package hu.greencode.nike2tcx;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class NikePlusConverter implements CommandLineRunner {

    public static void main(String[] args)  {
        new SpringApplicationBuilder().web(false).sources(NikePlusConverter.class).showBanner(false).run(args);
    }

    @Override
    public void run(String... strings) throws Exception {
//        System.out.print("What is your access token? ");
//        byte[] accessTokenByteArray = new byte[40];
//        System.in.read(accessTokenByteArray);
//        String accessToken = new String(accessTokenByteArray);

        new NikeDataReader(strings[0]).convert();
    }
}
