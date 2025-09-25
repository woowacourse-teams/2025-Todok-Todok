package todoktodok.backend.global.jwt;

import todoktodok.backend.global.auth.Role;

public record TokenInfo(
        Long id,
        String email,
        Role role
) {
}
