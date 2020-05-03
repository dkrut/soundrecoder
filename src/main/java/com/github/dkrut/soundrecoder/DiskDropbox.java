package com.github.dkrut.soundrecoder;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DiskDropbox {
    private static final Logger log = LoggerFactory.getLogger(DiskDropbox.class);
    private DbxRequestConfig config;
    private DbxClientV2 client;

    public DiskDropbox(String accessToken) {
        config = DbxRequestConfig.newBuilder("SoundRecoder").build();
        client = new DbxClientV2(config, accessToken);
    }

    public void upload(File fileName) {
        try {
            InputStream in = new FileInputStream(fileName);
            log.info("Uploading file '{}' to Dropbox...", fileName.getName());
            client.files().uploadBuilder("/" + fileName).uploadAndFinish(in);
            in.close();
        } catch (Exception ex) {
            log.error("Couldn't upload file: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
