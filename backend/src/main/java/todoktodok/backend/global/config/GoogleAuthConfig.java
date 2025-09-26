package todoktodok.backend.global.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class GoogleAuthConfig {

    @Value("${google.client-id}")
    private String googleClientId;

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier(
            final HttpTransport transport,
            final JsonFactory jsonFactory) {

        return new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    @Bean
    public HttpTransport httpTransport() {
        return new NetHttpTransport();
    }

    @Bean
    public JsonFactory jsonFactory() {
        return GsonFactory.getDefaultInstance();
    }
}
