package backend.goorm.record.dto;

import backend.goorm.record.entity.Record;
import backend.goorm.record.enums.Intensity;
import backend.goorm.training.model.entity.Training;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class EditRecordRequest {

    private final Long recordId;
    private final Integer durationMinutes;
    private final Intensity intensity;
    private final Integer sets;
    private final Integer weight;
    private final Integer reps;
    private final Float distance;
    private final String memo;
    private final Integer satisfaction;

    public static Record updateRecord(Record record, EditRecordRequest edit, boolean isCardio, Float calculatedCalories) {
        // 공통 업데이트 (null-safe)
        if (edit.durationMinutes != null) record.setDurationMinutes(edit.durationMinutes);
        if (edit.satisfaction != null) record.setSatisfaction(edit.satisfaction);
        record.setIntensity(edit.intensity != null ? edit.intensity.name() : null);

        if (isCardio) {
            if (edit.distance != null) record.setDistance(edit.distance);
            if (calculatedCalories != null) record.setCaloriesBurned(calculatedCalories);
        } else {
            if (edit.sets != null) record.setSets(edit.sets);
            if (edit.weight != null) record.setWeight(edit.weight);
            if (edit.reps != null) record.setReps(edit.reps);
        }
        // memo 등 추가 필드 필요시 처리

        return record;
    }
}
