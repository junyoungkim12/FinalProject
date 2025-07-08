package backend.goorm.training.service;

import backend.goorm.training.dto.AddTrainingRequest;
import backend.goorm.training.dto.TrainingDto;
import backend.goorm.training.model.entity.Training;
import backend.goorm.training.model.entity.TrainingCategory;
import backend.goorm.training.repository.TrainingCategoryRepository;
import backend.goorm.training.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class BasicTrainingService {

    private final TrainingRepository trainingRepository;
    private final TrainingCategoryRepository trainingCategoryRepository;

    public TrainingDto addBasicTraining(AddTrainingRequest request) {
        Long categoryId = request.getCategoryId();

        // 카테고리 존재 여부만 검증
        TrainingCategory category = trainingCategoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("카테고리 ID({})가 존재하지 않음", categoryId);
                    return new IllegalArgumentException("카테고리 ID가 존재하지 않습니다.");
                });

        Training training = Training.builder()
                .trainingName(request.getName())
                .category(category)
                .userCustom(false)
                .build();

        Training saved = trainingRepository.save(training);
        log.info("기본 운동 등록 완료: {}", saved.getTrainingName());
        return TrainingDto.fromEntity(saved);
    }
}
