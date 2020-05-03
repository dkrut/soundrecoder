package com.github.dkrut.soundrecoder;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class DiskDropbox {
    private static final Logger log = LoggerFactory.getLogger(DiskDropbox.class);
    private DbxRequestConfig config;
    private DbxClientV2 client;
    private Property property = new Property();

    public DiskDropbox() {
        config = DbxRequestConfig.newBuilder("SoundRecorder").build();
        log.info("Create Dropbox client");
        client = new DbxClientV2(config, property.getProperty("dropbox_access_token"));
    }

    public void uploadFile(File fileName, Boolean deleteAfter) {
        try {
            InputStream in = new FileInputStream(fileName);
            log.info("Uploading file '{}' to Dropbox...", fileName.getName());
            client.files().uploadBuilder("/" + fileName).uploadAndFinish(in);
            in.close();
            log.info("Uploading file '{}' to Dropbox finished", fileName.getName());
        } catch (Exception e) {
            log.error("Couldn't upload file '{}': " + e.getMessage(), fileName.getName());
            e.printStackTrace();
            log.warn("File left at '{}'", fileName.getAbsolutePath());
            return;
        }

        if (deleteAfter) {
            try {
                log.info("Delete local file version '{}'", fileName.getAbsolutePath());
                Files.deleteIfExists(fileName.toPath());
            } catch (IOException e) {
                log.warn("Couldn't delete file '{}': " + e.getMessage(), fileName.getName());
                e.printStackTrace();
            }
        } else log.info("File left at '{}'", fileName.getAbsolutePath());
    }
}
