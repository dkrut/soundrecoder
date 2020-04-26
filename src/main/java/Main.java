import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Denis Krutikov on 25.04.2020.
 */

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JavaSoundRecorder recorder = new JavaSoundRecorder(properties.getProperty("ACCESS_TOKEN"));
        long milliseconds = 60000;
        log.info("Recording length value = " + milliseconds + " milliseconds");

        int iterationsCount = Integer.parseInt(properties.getProperty("iterationsCount"));
        log.info("Iterations count = " + iterationsCount);

        try {
            for (int i = 0; i < iterationsCount; i++) {
                log.info("Start iteration #" + (i+1));
                recorder.recordSound(milliseconds);
                Thread.sleep(milliseconds + 100);
            }
        } catch (InterruptedException e) {
            log.error("Error in thread sleeping: " + e);
            e.printStackTrace();
        }
    }
}
