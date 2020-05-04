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
        long milliseconds = Integer.parseInt(property.getProperty("duration"));
        log.info("Recording length value = " + milliseconds + " milliseconds");

        int iterationsCount = Integer.parseInt(property.getProperty("iterationsCount"));
        log.info("Iterations count = " + iterationsCount);

        try {
            for (int i = 0; i < iterationsCount; i++) {
                log.info("Start iteration " + (i+1) + "/" + iterationsCount);
                recorder.recordSound(milliseconds);
                Thread.sleep(milliseconds);
            }
        } catch (InterruptedException e) {
            log.error("Error in thread sleeping: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
