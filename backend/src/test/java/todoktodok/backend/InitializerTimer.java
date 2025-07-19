package todoktodok.backend;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class InitializerTimer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    long startTimeMs;

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        startTimeMs = System.currentTimeMillis();
        applicationContext.addApplicationListener(e -> {
            if (e instanceof org.springframework.context.event.ContextRefreshedEvent) {
                long endTimeMs = System.currentTimeMillis();
                long durationMs = endTimeMs - startTimeMs;
                System.out.println("ApplicationContext 로딩 시간: " + durationMs + "ms");
            }
        });
    }
}