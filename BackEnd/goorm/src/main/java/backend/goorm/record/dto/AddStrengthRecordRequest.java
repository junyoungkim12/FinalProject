package backend.goorm.record.dto;

import backend.goorm.record.entity.Record;
import backend.goorm.training.model.entity.Training;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class AddStrengthRecordRequest {

    private final Integer durationMinutes;
    private final String intensity;
    private final Integer sets;
    private final Integer reps; // 횟수 추가
    private final Integer weight;
    private final Integer satisfaction;
    private final LocalDate exerciseDate; // 운동 날짜 추가

    public static Record toEntity(AddStrengthRecordRequest request, Training training) {
        return Record.builder()
                .training(training)
                .caloriesBurned(0f)
                .durationMinutes(request.durationMinutes)
                .intensity(request.intensity)
                .sets(request.sets)
                .reps(request.reps)
                .weight(request.weight)
                .exerciseDate(request.exerciseDate != null ? request.exerciseDate : LocalDate.now())
                .recordDate(LocalDateTime.now())
                .satisfaction(request.satisfaction)
                .build();
    }
}