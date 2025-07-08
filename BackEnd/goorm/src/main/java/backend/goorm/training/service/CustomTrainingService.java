package backend.goorm.training.service;

import backend.goorm.member.model.entity.Member;
import backend.goorm.member.repository.MemberRepository;
import backend.goorm.training.dto.TrainingDto;
import backend.goorm.training.model.entity.Training;
import backend.goorm.training.dto.AddTrainingRequest;
import backend.goorm.training.dto.EditTrainingRequest;
import backend.goorm.training.model.entity.TrainingCategory;
import backend.goorm.training.repository.TrainingCategoryRepository;
import backend.goorm.training.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomTrainingService {

    private final MemberRepository memberRepository;
    private final TrainingRepository trainingRepository;
    private final TrainingCategoryRepository trainingCategoryRepository;

    // 커스텀 운동 등록
    public TrainingDto addCustomTraining(AddTrainingRequest request, Member member) {
        // 1. 카테고리 ID로만 검증 (category 객체 대신)
        Long categoryId = request.getCategoryId();
        TrainingCategory category = trainingCategoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    log.warn("카테고리 ID({})가 존재하지 않음", categoryId);
                    return new IllegalArgumentException("카테고리 ID가 존재하지 않습니다.");
                });

        // 2. Builder 패턴 활용하여 Training 생성 (setter 사용 X)
        Training training = Training.builder()
                .trainingName(request.getName())
                .category(category)
                .member(member)
                .userCustom(true)
                .build();

        Training saved = trainingRepository.save(training);
        log.info("커스텀 운동 등록 완료: memberId={}, trainingId={}", member.getMemberId(), saved.getTrainingId());
        return TrainingDto.fromEntity(saved);
    }

    // 커스텀 운동 수정
    public TrainingDto editCustomTraining(EditTrainingRequest request, Member member) {
        Training training = trainingRepository.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Training not found with id: " + request.getId()));

        // 사용자 소유 확인
        validateMemberOwnership(training, member);

        // 카테고리 수정도 ID로만
        Long categoryId = request.getCategoryId();
        TrainingCategory category = trainingCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 ID가 존재하지 않습니다."));

        // Setter 대신 Builder/Copy를 권장, 하지만 JPA에서는 실무적으로 Setter도 필요할 수 있음
        training.updateTraining(request.getTrainingName(), category);
        Training saved = trainingRepository.save(training);
        log.info("커스텀 운동 수정 완료: memberId={}, trainingId={}", member.getMemberId(), saved.getTrainingId());
        return TrainingDto.fromEntity(saved);
    }

    // 운동 삭제
    public TrainingDto deleteCustomTraining(Long trainingId, Member member) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("Training not found with id: " + trainingId));

        validateMemberOwnership(training, member);

        if (Boolean.TRUE.equals(training.getUserCustom())) {
            trainingRepository.delete(training);
            log.info("커스텀 운동 삭제 완료: memberId={}, trainingId={}", member.getMemberId(), trainingId);
            return TrainingDto.fromEntity(training);
        }
        throw new IllegalArgumentException("기본 운동은 삭제할 수 없습니다.");
    }

    // 회원별 커스텀 운동 목록
    public List<TrainingDto> customTrainingList(Member member) {
        return trainingRepository.findByMember_MemberId(member.getMemberId())
                .stream()
                .map(TrainingDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 소유자 확인 메서드 분리
    private void validateMemberOwnership(Training training, Member member) {
        if (!training.getMember().getMemberId().equals(member.getMemberId())) {
            log.warn("본인이 만든 커스텀 운동이 아닙니다: 요청 memberId={}, 실제 memberId={}", member.getMemberId(), training.getMember().getMemberId());
            throw new IllegalArgumentException("이 훈련은 현재 사용자와 관련이 없습니다.");
        }
    }
}
