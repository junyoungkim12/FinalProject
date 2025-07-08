package backend.goorm.training.controller;

import backend.goorm.training.dto.AddTrainingRequest;
import backend.goorm.training.dto.TrainingDto;
import backend.goorm.training.service.BasicTrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/trainings")
public class BasicTrainingController {

    private final BasicTrainingService basicTrainingService;

    /**
     * 기본 운동 등록 API
     */
    @PostMapping
    public ResponseEntity<TrainingDto> addBasicTraining(@RequestBody AddTrainingRequest input) {
        log.info("기본 운동 등록 요청: {}", input);
        TrainingDto result = basicTrainingService.addBasicTraining(input);
        return ResponseEntity.ok(result);
    }

    /**
     * 전체 기본 운동 목록 조회 API
     */
    @GetMapping
    public ResponseEntity<List<TrainingDto>> getAllTrainings() {
        List<TrainingDto> trainings = basicTrainingService.getAllTrainings();
        return ResponseEntity.ok(trainings);
    }
}
