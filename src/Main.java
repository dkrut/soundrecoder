import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Denis Krutikov on 25.04.2020.
 */

public class Main {
    public static void main(String[] args) {
        String ACCESS_TOKEN;

        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        JavaSoundRecorder recorder = new JavaSoundRecorder();

        String fileName = "out/file.wav";
        recorder.recordSound(10000, fileName);

        try {
            Thread.sleep(15000);
            InputStream in = new FileInputStream(fileName);
            client.files().uploadBuilder("/" + fileName).uploadAndFinish(in);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
