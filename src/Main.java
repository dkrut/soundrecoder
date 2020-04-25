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
        long milliseconds = 60000;
        try {
            for (int i = 0; i < Integer.parseInt(properties.getProperty("iterationsCount")); i++) {
                recorder.recordSound(milliseconds);
                Thread.sleep(milliseconds + 100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
