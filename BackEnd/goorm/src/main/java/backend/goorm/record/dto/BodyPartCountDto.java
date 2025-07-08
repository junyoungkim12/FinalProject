package backend.goorm.record.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BodyPartCountDto {
    private double cardio;
    private double chest;
    private double back;
    private double legs;
    private double shoulder;
    private double biceps;
    private double triceps;
    private double abs;
    private double etc;
}
