package backend.goorm.diet.dto;

import backend.goorm.diet.entity.Food;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodUserDto {
    private String foodType;
    private String foodName;
    private Float gram;
    private Float calories;
    private Float carbohydrate;
    private Float protein;
    private Float fat;
    private Float sugar;
    private Float salt;
    private Float cholesterol;
    private Float saturatedFat;
    private Float transFat;
    private String imageUrl;
    @Builder.Default
    private Integer useCount = 0;
    @Builder.Default
    private Boolean userRegister = true;

    public Food toEntity() {
        return Food.builder()
                .foodName(this.foodName)
                .gram(this.gram)
                .calories(this.calories)
                .carbohydrate(this.carbohydrate)
                .protein(this.protein)
                .fat(this.fat)
                .sugar(this.sugar)
                .salt(this.salt)
                .cholesterol(this.cholesterol)
                .saturatedFat(this.saturatedFat)
                .transFat(this.transFat)
                .useCount(this.useCount)
                .userRegister(this.userRegister)
                .build();
    }
}