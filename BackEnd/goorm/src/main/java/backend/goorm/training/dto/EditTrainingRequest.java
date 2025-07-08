package backend.goorm.training.dto;

import backend.goorm.training.model.entity.TrainingCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EditTrainingRequest {
    private final Long id;
    private final String trainingName;
    private final Long categoryId;

    public EditTrainingRequest(Long id, String trainingName, Long categoryId) {
        this.id = id;
        this.trainingName = trainingName;
        this.categoryId = categoryId;
    }
}
