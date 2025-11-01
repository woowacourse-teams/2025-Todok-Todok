package todoktodok.backend.global.scheduler.dto;

public record TableFragmentationInfo(
        String tableName,
        Double dataSizeMb,
        Double indexSizeMb,
        Double fragmentedMb,
        Double fragmentationPercentage
) {
}
