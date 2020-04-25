import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'_'HHmmss");
        String fileName = formatter.format(new Date(System.currentTimeMillis())) + ".wav";

        recorder.recordSound(10000, fileName);
    }
}
