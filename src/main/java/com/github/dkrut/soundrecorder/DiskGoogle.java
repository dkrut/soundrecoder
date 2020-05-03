package com.github.dkrut.soundrecorder;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class DiskGoogle {

    private static final Logger log = LoggerFactory.getLogger(DiskGoogle.class);
    private static final String APPLICATION_NAME = "SoundRecorder";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final java.io.File CREDENTIALS_FOLDER = new java.io.File("src/main/resources");

    private NetHttpTransport HTTP_TRANSPORT;
    private java.io.File clientSecretFilePath = new java.io.File("src/main/resources/client_secrets.json");
    private GoogleClientSecrets clientSecrets;
    private GoogleAuthorizationCodeFlow flow;
    private Credential credential;
    private Drive drive;

    public DiskGoogle() {
        try {
            log.debug("Build a new authorized Google API client service");
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            log.debug("Load client secrets");
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new FileInputStream(clientSecretFilePath)));
            log.debug("Build flow and trigger user authorization request");
            flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    clientSecrets, Collections.singleton(DriveScopes.DRIVE_FILE))
                    .setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
                    .setAccessType("offline").build();
            log.debug("Read client_secret.json file");
            credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        } catch (GeneralSecurityException e) {
            log.error("Couldn't get Google transport: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Couldn't authorize: " + e.getMessage());
            e.printStackTrace();
        }
        log.info("Create Google Drive service");
        drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    public void uploadFile(java.io.File fileName, Boolean deleteAfter) {
        File fileMetadata = new File();
        fileMetadata.setName(fileName.getName());
        FileContent mediaContent = new FileContent("media/wave", fileName);
        try {
            log.info("Uploading file '{}' to Google Disk...", fileName.getName());
            drive.files().create(fileMetadata, mediaContent).execute();
            log.info("Uploading file '{}' to Google Disk finished", fileName.getName());
        } catch (IOException e) {
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