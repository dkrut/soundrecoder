import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JavaSoundRecorder
{
    private AudioFileFormat.Type fileType;
    private TargetDataLine line;
    private AudioFormat audioFormat;
    private DbxRequestConfig config;
    private DbxClientV2 client;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'_'HHmmss");

    public JavaSoundRecorder(String accessToken) {
        fileType = AudioFileFormat.Type.WAVE;
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        client = new DbxClientV2(config, accessToken);
    }

    public void recordSound(long milliseconds) {
        File file = new File(formatter.format(new Date(System.currentTimeMillis())) + ".wav");
        start(file);
        delayFinish(file, milliseconds);
    }

    private void start(File file) {
        new Thread(() ->
        {
            try {
                line.open(audioFormat);
                line.start();
                AudioInputStream ais = new AudioInputStream(line);
                AudioSystem.write(ais, fileType, file);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void delayFinish(File fileName, long delayTime) {
        new Thread(() ->
        {
            try
            {
                Thread.sleep(delayTime);
                line.stop();
                line.close();
                try {
                    InputStream in = new FileInputStream(fileName);
                    client.files().uploadBuilder("/" + fileName).uploadAndFinish(in);
                    in.close();
                    Files.deleteIfExists(fileName.toPath());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}