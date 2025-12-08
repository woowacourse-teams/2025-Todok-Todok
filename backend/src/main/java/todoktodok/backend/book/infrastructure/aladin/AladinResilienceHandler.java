package todoktodok.backend.book.infrastructure.aladin;

import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Component;
import todoktodok.backend.book.infrastructure.aladin.exception.AladinApiException;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class AladinResilienceHandler {

    /*
    원본 로직, Resilience 로직, Fallback 로직을 인자로 전달하면, 원본 로직을 실행하면서 실패 시 Resilience과 fallback을 함께 실행한다.
    */
    public <T> T applyWithResilienceAndReturnOrFallback(
            final Supplier<T> origin,
            final Function<Supplier<T>, CompletableFuture<T>> resilience,
            final Function<Exception, T> fallback
    ) {
        try {
            return resilience.apply(origin).join();
        } catch (final Exception e) {
            return fallback.apply(e);
        }
    }

    /*
    원본 로직, Resilience 로직을 인자로 전달하면, 원본 로직을 실행하면서 실패 시 Resilience을 함께 실행한다.
    */
    public <T> T applyWithResilienceAndReturnOrFail(
            final Supplier<T> origin,
            final Function<Supplier<T>, CompletableFuture<T>> resilience
    ) {
        try {
            return resilience.apply(origin).join();
        } catch (final Exception e) {
            throw new AladinApiException("도서 검색 API에 일시적 장애가 발생해 이용이 불가능합니다");
        }
    }

    @Retry(name = "bookSearch")
    @TimeLimiter(name = "bookSearch")
    public <T> CompletableFuture<T> applyBookSearch(
            Supplier<T> supplier
    ) {
        return CompletableFuture.supplyAsync(supplier);
    }
}
