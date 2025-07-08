package backend.goorm.diet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
