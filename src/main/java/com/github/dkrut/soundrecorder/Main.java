package com.github.dkrut.soundrecorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Denis Krutikov on 25.04.2020.
 */

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        SoundRecorder recorder = new SoundRecorder();
        Property property = new Property();

        String cloud = property.getProperty("cloud").toLowerCase();
        log.info("Cloud to upload file: " + cloud);

        long milliseconds = Integer.parseInt(property.getProperty("duration"));
        log.info("Recording duration: " + milliseconds + " milliseconds");

        boolean deleteAfter = Boolean.parseBoolean(property.getProperty("deleteAfter"));
        log.info("Delete file after uploading to cloud '{}': " + deleteAfter, cloud);

        int iterationsCount = Integer.parseInt(property.getProperty("iterationsCount"));
        log.info("Iterations count: " + iterationsCount);

        try {
            for (int i = 0; i < iterationsCount; i++) {
                log.info("Start iteration " + (i+1) + "/" + iterationsCount);
                recorder.recordSound(milliseconds, cloud, deleteAfter);
                Thread.sleep(milliseconds);
            }
        } catch (InterruptedException e) {
            log.error("Error in thread sleeping: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
