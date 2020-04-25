import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Denis Krutikov on 25.04.2020.
 */

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JavaSoundRecorder recorder = new JavaSoundRecorder(properties.getProperty("ACCESS_TOKEN"));

        recorder.recordSound(60000);
    }
}
