import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JavaSoundRecorder
{
    private static final Logger log = LoggerFactory.getLogger(JavaSoundRecorder.class);
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
                log.debug("Open line");
                line.open(audioFormat);
                log.debug("Start line");
                line.start();
                AudioInputStream ais = new AudioInputStream(line);
                log.info("Start recording file '{}'", file.getName());
                AudioSystem.write(ais, fileType, file);
                log.info("Recording file '{}' finished", file.getName());
            } catch (Exception ex) {
                log.error("Error during recording '{}': " + ex, file.getName());
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
                log.debug("Line stop");
                line.stop();
                log.debug("Line close");
                line.close();
                try {
                    InputStream in = new FileInputStream(fileName);
                    log.info("Upload file '{}' to dropbox", fileName.getName());
                    client.files().uploadBuilder("/" + fileName).uploadAndFinish(in);
                    in.close();
                    log.info("Delete local file version '{}'", fileName.getAbsolutePath());
                    Files.deleteIfExists(fileName.toPath());
                } catch (Exception ex) {
                    log.error("Error: " + ex);
                    ex.printStackTrace();
                }
            }
            catch (InterruptedException e) {
                log.error("Error in thread sleeping: " + e);
                e.printStackTrace();
            }
        }).start();
    }
}