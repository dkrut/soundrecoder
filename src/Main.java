/**
 * Created by Denis Krutikov on 25.04.2020.
 */

public class Main {
    public static void main(String[] args) {
        JavaSoundRecorder recorder = new JavaSoundRecorder();
        recorder.recordSound(10000, "out/file.wav");
    }
}
