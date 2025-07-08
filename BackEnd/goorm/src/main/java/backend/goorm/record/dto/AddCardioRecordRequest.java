package backend.goorm.record.dto;

import backend.goorm.record.entity.Record;
import backend.goorm.record.enums.Intensity;
import backend.goorm.training.model.entity.Training;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class AddCardioRecordRequest {

    private final Integer durationMinutes;
    private final Intensity intensity; // 변경된 부분
    private final Float distance;
    private final Integer satisfaction;
    private final LocalDate exerciseDate;

    public static Record toEntity(AddCardioRecordRequest request, Training training, Float calculatedCalories) {
        return Record.builder()
                .training(training)
                .caloriesBurned(calculatedCalories)
                .durationMinutes(request.durationMinutes)
                .intensity(request.intensity != null ? request.intensity.name() : null)
                .distance(request.distance)
                .exerciseDate(request.exerciseDate != null ? request.exerciseDate : LocalDate.now())
                .recordDate(LocalDateTime.now())
                .satisfaction(request.satisfaction)
                .build();
    }
}
