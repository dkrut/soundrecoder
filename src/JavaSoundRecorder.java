import javax.sound.sampled.*;
import java.io.*;

public class JavaSoundRecorder {
    private AudioFileFormat.Type fileType;
    private TargetDataLine line;
    private AudioFormat audioFormat;

    public JavaSoundRecorder() {
        fileType = AudioFileFormat.Type.WAVE;
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        audioFormat = new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void recordSound(long milliseconds, String filePath) {
        File file = new File(filePath);
        start(file);
        //...
        finish();
    }

    private void start(File file) {
        try {
            line.open(audioFormat);
            line.start();
            AudioInputStream ais = new AudioInputStream(line);
            AudioSystem.write(ais, fileType, file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void finish() {
        line.stop();
        line.close();
    }
}