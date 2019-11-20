package me.pckv.kompis;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Log
@Service
public class FcmInitializer {

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    @Value("${app.firebase-database-url}")
    private String firebaseDatabaseUrl;

    @PostConstruct
    public void initialize() {
        log.info("Loading firebase configuration from: " + firebaseConfigPath);
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials
                            .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))
                    .setDatabaseUrl(firebaseDatabaseUrl)
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("Firebase application has been initialized");
            }
        }
        catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

}
