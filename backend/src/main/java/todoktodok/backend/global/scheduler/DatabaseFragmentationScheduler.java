package todoktodok.backend.global.scheduler;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import todoktodok.backend.global.scheduler.dto.TableFragmentationInfo;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "database.fragmentation-check.enabled", havingValue = "true")
public class DatabaseFragmentationScheduler {

    private final EntityManager entityManager;

    @Scheduled(cron = "${database.fragmentation-check.cron}")
    public void checkFragmentation() {
        log.info("데이터베이스 단편화 점검 시작");

        final List<TableFragmentationInfo> fragmentedTables = getFragmentedTables();

        if (fragmentedTables.isEmpty()) {
            log.info("단편화된 테이블이 없습니다");
            return;
        }

        log.warn("단편화가 감지된 테이블 수: {}", fragmentedTables.size());
        fragmentedTables.forEach(this::logFragmentationInfo);
    }

    private List<TableFragmentationInfo> getFragmentedTables() {
        final String sql = """
                SELECT
                    table_name,
                    ROUND(data_length / 1024 / 1024, 2) AS data_size_mb,
                    ROUND(index_length / 1024 / 1024, 2) AS index_size_mb,
                    ROUND(data_free / 1024 / 1024, 2) AS fragmented_mb,
                    ROUND((data_free / (data_length + index_length)) * 100, 2) AS fragmentation_pct
                FROM information_schema.tables
                WHERE table_schema = DATABASE()
                    AND data_free > 0
                    AND (data_length + index_length) > 0
                    AND (data_free / (data_length + index_length)) * 100 > :threshold
                ORDER BY fragmentation_pct DESC
                """;

        final Query query = entityManager.createNativeQuery(sql);
        query.setParameter("threshold", getFragmentationThreshold());

        @SuppressWarnings("unchecked")
        final List<Object[]> results = query.getResultList();

        return results.stream()
                .map(this::mapToFragmentationInfo)
                .toList();
    }

    private TableFragmentationInfo mapToFragmentationInfo(final Object[] row) {
        return new TableFragmentationInfo(
                (String) row[0],
                (Double) row[1],
                (Double) row[2],
                (Double) row[3],
                (Double) row[4]
        );
    }

    private void logFragmentationInfo(final TableFragmentationInfo info) {
        log.warn(
                "테이블: {}, 데이터 크기: {}MB, 인덱스 크기: {}MB, 단편화 크기: {}MB, 단편화율: {}%",
                info.tableName(),
                info.dataSizeMb(),
                info.indexSizeMb(),
                info.fragmentedMb(),
                info.fragmentationPercentage()
        );
    }

    private double getFragmentationThreshold() {
        return 10.0;
    }
}
