/**
 * Created by Denis Krutikov on 25.04.2020.
 */

public class Main {
    public static void main(String[] args) {
        String ACCESS_TOKEN = ""; //TODO: config with dropbox token

        JavaSoundRecorder recorder = new JavaSoundRecorder(ACCESS_TOKEN);

        String fileName = "out/file.wav";  //TODO: change file name to current date
        recorder.recordSound(10000, fileName);
    }
}
