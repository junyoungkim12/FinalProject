package backend.goorm.training.dto;

import backend.goorm.training.model.entity.Training;
import backend.goorm.training.model.entity.TrainingCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
public class AddTrainingRequest {
    private final String name;
    private final Long categoryId;

    public AddTrainingRequest(String name, Long categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }

    public Training toEntity(TrainingCategory category) {
        return Training.builder()
                .trainingName(name)
                .category(category)
                .userCustom(true)
                .build();
    }
}

