package com.aakash.gmailreader;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GmailReader {

    private static final String APPLICATION_NAME = "Gmail Reader";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
         InputStream in = new FileInputStream(System.getenv("google-credentials"));
         GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
         return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        String user = "me";
        ListMessagesResponse listResponse = service.users().messages().list(user).setMaxResults(200L).execute();
        List<Message> messages = listResponse.getMessages();
        int count=1;
        if (messages.isEmpty()) {
            System.out.println("No messages found.");
        } else {

            for (Message message : messages) {
                Message messageObj = service.users().messages().get(user, message.getId()).execute();
                Map<String,String> messageMap = getMetaData(messageObj);
                System.out.printf("S.N %d, Sender Name: %s, Subject: %s \n",count++,messageMap.getOrDefault("From","Unknown"),messageMap.getOrDefault("Subject","Subject not found"));
            }
        }
    }

     private static Map<String ,String> getMetaData(Message message) {
         Map<String,String> messageMap  =new HashMap<>();
         List<MessagePartHeader> headers = message.getPayload().getHeaders();
         for (MessagePartHeader header : headers) {
             if ("From".equalsIgnoreCase(header.getName())) {
                 String from = header.getValue();
                    messageMap.put("From",from);
                 }

             if("Subject".equalsIgnoreCase(header.getName())){
                 messageMap.put("Subject",header.getValue());
             }
         }
         return  messageMap;
     }
}