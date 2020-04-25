import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Denis Krutikov on 25.04.2020.
 */

public class Main {
    public static void main(String[] args) {
        String ACCESS_TOKEN = ""; //TODO: config with dropbox token

        JavaSoundRecorder recorder = new JavaSoundRecorder(ACCESS_TOKEN);

        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMdd'_'HHmmss");
        Date date = new Date(System.currentTimeMillis());

        String fileName = formatter.format(date) + ".wav";
        recorder.recordSound(10000, fileName);



    }
}
