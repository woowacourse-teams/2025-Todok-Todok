package todoktodok.backend.global.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import todoktodok.backend.member.domain.Member;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AdminProperties {

    private final Set<String> emails;

    public AdminProperties(
            @Value("${admin.email-path}") Resource adminEmailResources
    ) throws IOException {
        try (InputStream in = adminEmailResources.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            this.emails = br.lines()
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
        }
    }

    public boolean checkIsAdmin(final Member member) {
        return emails.contains(member.getEmail());
    }
}
