package backend.goorm.record.dto;

import backend.goorm.record.entity.Record;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class RecordDto {
    private final Long recordId;
    private final Float caloriesBurned;
    private final Integer durationMinutes;
    private final String intensity;
    private final Integer sets;
    private final Integer reps;
    private final Integer weight;
    private final Float distance;
    private final String memo;
    private final Integer satisfaction;
    private final String trainingName;
    private final LocalDate exerciseDate;
    private final String categoryName;
    private final LocalDateTime modifiedDate;
    private final Float totalCaloriesBurned;

    public static RecordDto fromEntity(Record record, String memo, Float totalCaloriesBurned) {
        return RecordDto.builder()
                .recordId(record.getRecordId())
                .caloriesBurned(record.getCaloriesBurned())
                .durationMinutes(record.getDurationMinutes())
                .intensity(record.getIntensity())
                .sets(record.getSets())
                .reps(record.getReps())
                .weight(record.getWeight())
                .distance(record.getDistance())
                .memo(memo)
                .satisfaction(record.getSatisfaction())
                .trainingName(record.getTraining() != null ? record.getTraining().getTrainingName() : null)
                .exerciseDate(record.getExerciseDate())
                .categoryName(record.getTraining() != null && record.getTraining().getCategory() != null
                        ? String.valueOf(record.getTraining().getCategory().getCategoryName())
                        : null)
                .modifiedDate(record.getModifiedDate())
                .totalCaloriesBurned(totalCaloriesBurned)
                .build();
    }

    public static RecordDto fromEntity(Record record, String memo) {
        return fromEntity(record, memo, null);
    }

    public static RecordDto fromEntity(Record record) {
        return fromEntity(record, null, null);
    }
}



