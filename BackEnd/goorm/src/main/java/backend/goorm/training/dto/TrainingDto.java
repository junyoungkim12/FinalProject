package backend.goorm.training.dto;

import backend.goorm.training.model.entity.Training;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TrainingDto {
    private final Long id;
    private final String name;
    private final String categoryName;
    private final Long categoryId;

    private TrainingDto(Long id, String name, String categoryName, Long categoryId) {
        this.id = id;
        this.name = name;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }

    public static TrainingDto fromEntity(Training training) {
        String categoryName = training.getCategory() != null ?
                training.getCategory().getCategoryName().name() : "Unknown";
        Long categoryId = training.getCategory() != null ?
                training.getCategory().getCategoryId() : null;
        return new TrainingDto(
                training.getTrainingId(),
                training.getTrainingName(),
                categoryName,
                categoryId
        );
    }
}



