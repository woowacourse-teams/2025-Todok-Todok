package todoktodok.backend.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConditionalOnProperty(prefix = "firebase", name = "enabled", havingValue = "true")
public class FirebaseConfig {

    @Value("${firebase.account.path}")
    private Resource serviceAccountJson;

    @Bean
    public FirebaseApp initialize() throws IOException {
        for (FirebaseApp app : FirebaseApp.getApps()) {
            if (FirebaseApp.DEFAULT_APP_NAME.equals(app.getName())) {
                return FirebaseApp.getInstance();
            }
        }

        try (InputStream in = serviceAccountJson.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(in))
                    .build();
            return FirebaseApp.initializeApp(options);
        }
    }
}
