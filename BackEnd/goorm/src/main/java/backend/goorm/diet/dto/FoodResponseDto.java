package backend.goorm.diet.dto;

import backend.goorm.diet.entity.Food;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodResponseDto {
    private Long foodId;
    private String userName;
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
    private Integer useCount;
    private Boolean userRegister;

    public static FoodResponseDto fromEntity(Food food) {
        return FoodResponseDto.builder()
                .foodId(food.getFoodId())
                .userName(food.getMember() != null ? food.getMember().getMemberNickname() : "admin")
                .foodName(food.getFoodName())
                .gram(food.getGram())
                .calories(food.getCalories())
                .carbohydrate(food.getCarbohydrate())
                .protein(food.getProtein())
                .fat(food.getFat())
                .sugar(food.getSugar())
                .salt(food.getSalt())
                .cholesterol(food.getCholesterol())
                .saturatedFat(food.getSaturatedFat())
                .transFat(food.getTransFat())
                .useCount(food.getUseCount())
                .userRegister(food.getUserRegister())
                .build();
    }

    public static List<FoodResponseDto> fromEntityList(List<Food> foods) {
        return foods.stream()
                .map(FoodResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
}