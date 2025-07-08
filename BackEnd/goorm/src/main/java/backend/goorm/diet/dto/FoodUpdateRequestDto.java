package backend.goorm.diet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodUpdateRequestDto {
        private String foodName;
        private Float calories;
        private Float carbohydrate;
        private Float protein;
        private Float fat;
        private Float sugar;
        private Float salt;
        private Float cholesterol;
        private Float saturatedFat;
        private Float transFat;
}

