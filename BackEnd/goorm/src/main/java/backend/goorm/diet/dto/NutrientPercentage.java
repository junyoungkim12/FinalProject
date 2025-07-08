package backend.goorm.diet.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutrientPercentage {
    private double carbsPercentage;
    private double proteinPercentage;
    private double fatPercentage;
    private double totalCalories;
}
