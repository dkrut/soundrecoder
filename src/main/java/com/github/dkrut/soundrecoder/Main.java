package com.github.dkrut.soundrecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Denis Krutikov on 25.04.2020.
 */

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        JavaSoundRecorder recorder = new JavaSoundRecorder();
        long milliseconds = 60000;
        log.info("Recording length value = " + milliseconds + " milliseconds");

        Property property = new Property();
        int iterationsCount = Integer.parseInt(property.getProperty("iterationsCount"));
        log.info("Iterations count = " + iterationsCount);

        try {
            for (int i = 0; i < iterationsCount; i++) {
                log.info("Start iteration " + (i+1) + "/" + iterationsCount);
                recorder.recordSound(milliseconds);
                Thread.sleep(milliseconds + 100);
            }
        } catch (InterruptedException e) {
            log.error("Error in thread sleeping: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
