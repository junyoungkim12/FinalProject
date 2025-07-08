package backend.goorm.diet.dto;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString
public class DietUpdateRequestDto {
    private Long dietId;
    private LocalDate dietDate;
    private String mealTime;
    private List<FoodQuantity> foodQuantities;
    private String memo;

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @ToString
    public static class FoodQuantity {
        private Long foodId;
        private Float quantity;
        private Float gram;
    }

}
