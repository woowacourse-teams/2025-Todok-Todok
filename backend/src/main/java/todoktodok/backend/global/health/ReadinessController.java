package todoktodok.backend.global.health;

import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/readiness")
public class ReadinessController {

    private final ApplicationContext context;

    @PostMapping("/down")
    public ResponseEntity<Map<String, String>> down() {
        AvailabilityChangeEvent.publish(
                context,
                ReadinessState.REFUSING_TRAFFIC
        );

        return ResponseEntity.ok(Map.of(
                        "status", "DOWN",
                        "message", "Application is now refusing traffic"
                )
        );
    }

    @PostMapping("/up")
    public ResponseEntity<Map<String, String>> up() {
        AvailabilityChangeEvent.publish(
                context,
                ReadinessState.ACCEPTING_TRAFFIC
        );

        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "message", "Application is now accepting traffic"
        ));
    }
}
