package backend.goorm.record.service;

import backend.goorm.record.dto.EditRecordRequest;
import backend.goorm.record.dto.RecordDto;
import backend.goorm.record.entity.Memo;
import backend.goorm.record.entity.Record;
import backend.goorm.record.repository.MemoRepository;
import backend.goorm.record.repository.RecordRepository;
import backend.goorm.record.dto.AddCardioRecordRequest;
import backend.goorm.record.dto.AddStrengthRecordRequest;
import backend.goorm.member.model.entity.Member;
import backend.goorm.record.util.ExerciseCalculator;
import backend.goorm.training.model.entity.Training;
import backend.goorm.training.repository.TrainingRepository;
import backend.goorm.s3.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordService {

    private final RecordRepository recordRepository;
    private final TrainingRepository trainingRepository;
    private final MemoRepository memoRepository;
    private final ExerciseCalculator exerciseCalculator;

    @Transactional
    public RecordDto addCardioRecord(Long trainingId, AddCardioRecordRequest request, Member member, MultipartFile[] images) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 운동을 찾지 못했습니다.: " + trainingId));

        Float caloriesBurned = null;
        String trainingName = training.getTrainingName().toLowerCase();
        if (trainingName.contains("러닝")) {
            caloriesBurned = exerciseCalculator.calculateCaloriesForRunning(member, String.valueOf(request.getIntensity()), request.getDurationMinutes().floatValue());
        } else if (trainingName.contains("걷기")) {
            caloriesBurned = exerciseCalculator.calculateCaloriesForWalking(member, String.valueOf(request.getIntensity()), request.getDurationMinutes().floatValue());
        } else if (trainingName.contains("자전거 타기")) {
            caloriesBurned = exerciseCalculator.calculateCaloriesForCycling(member, String.valueOf(request.getIntensity()), request.getDurationMinutes().floatValue());
        } else if (trainingName.contains("계단 오르기")) {
            caloriesBurned = exerciseCalculator.calculateCaloriesForStairClimbing(member, String.valueOf(request.getIntensity()), request.getDurationMinutes().floatValue());
        }
        Record record = AddCardioRecordRequest.toEntity(request, training, caloriesBurned);
        record.setMember(member);

        Record saved = recordRepository.save(record);
        return RecordDto.fromEntity(saved);
    }

    @Transactional
    public RecordDto addStrengthRecord(Long trainingId, AddStrengthRecordRequest request, Member member, MultipartFile[] images) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 운동을 찾지 못했습니다.: " + trainingId));

        Record record = AddStrengthRecordRequest.toEntity(request, training);
        record.setMember(member);

        Record saved = recordRepository.save(record);
        return RecordDto.fromEntity(saved);
    }

    @Transactional
    public List<RecordDto> editRecords(List<EditRecordRequest> requests, Member member) {
        List<RecordDto> updatedRecords = new ArrayList<>();

        for (EditRecordRequest request : requests) {
            Record record = recordRepository.findById(request.getRecordId())
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 id의 운동기록을 찾지 못했습니다." + request.getRecordId()));

            // 권한 확인
            if (!record.getMember().getMemberId().equals(member.getMemberId())) {
                throw new IllegalArgumentException("자신의 운동 기록만 수정할 수 있습니다.");
            }

            Training training = record.getTraining();
            String categoryName = String.valueOf(training.getCategory().getCategoryName());

            boolean isCardio = "유산소".equalsIgnoreCase(categoryName);
            Float calculatedCalories = null;

            if (isCardio) {
                calculatedCalories = exerciseCalculator.calculateCaloriesForRunning(member, request.getIntensity().name(), request.getDurationMinutes().floatValue());
            }

            EditRecordRequest.updateRecord(record, request, isCardio, calculatedCalories);

            // 메모 업데이트 또는 추가 (별도 메서드 분리 가능)
            upsertMemoForRecord(request.getMemo(), record.getExerciseDate(), member);

            Record saved = recordRepository.save(record);
            String memoContent = request.getMemo() != null && !request.getMemo().isEmpty() ? request.getMemo() : null;

            updatedRecords.add(RecordDto.fromEntity(saved, memoContent));
        }

        return updatedRecords;
    }

    // 메모 저장/업데이트 로직 분리
    private void upsertMemoForRecord(String memoContent, LocalDate date, Member member) {
        if (memoContent == null) return;
        Optional<Memo> existingMemoOpt = memoRepository.findByMemberAndDate(member, date);

        if (existingMemoOpt.isPresent()) {
            Memo existingMemo = existingMemoOpt.get();
            existingMemo.setContent(memoContent);
            memoRepository.save(existingMemo);
        } else {
            Memo newMemo = Memo.builder()
                    .member(member)
                    .content(memoContent)
                    .date(date)
                    .build();
            memoRepository.save(newMemo);
        }
    }

    @Transactional
    public void deleteRecord(Long recordId, Member member) {
        Record record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 id의 운동기록을 찾지 못했습니다." + recordId));

        if (!record.getMember().getMemberId().equals(member.getMemberId())) {
            throw new IllegalArgumentException("해당 기록을 삭제할 권한이 없습니다.");
        }

        recordRepository.delete(record);
    }

    @Transactional(readOnly = true)
    public Page<RecordDto> getPagedRecords(Member member, Pageable pageable) {
        Page<Record> records = recordRepository.findAllByMember(member, pageable);

        return records.map(record -> {
            String memoContent = memoRepository.findByMemberAndDate(member, record.getExerciseDate())
                    .map(Memo::getContent)
                    .orElse(null);
            return RecordDto.fromEntity(record, memoContent);
        });
    }

    @Transactional(readOnly = true)
    public List<RecordDto> getAllRecords(Member member) {
        List<Record> records = recordRepository.findAllByMember(member);

        Float totalCaloriesBurned = records.stream()
                .map(Record::getCaloriesBurned)
                .reduce(0f, Float::sum);

        return records.stream().map(record -> {
            String memoContent = memoRepository.findByMemberAndDate(member, record.getExerciseDate())
                    .map(Memo::getContent)
                    .orElse(null);

            return RecordDto.fromEntity(record, memoContent, totalCaloriesBurned);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public int getTotalCaloriesBurnedByDateAndMember(LocalDate date, Member member) {
        return recordRepository.findAllByExerciseDateAndMember(date, member).stream()
                .mapToInt(record -> record.getCaloriesBurned() != null ? record.getCaloriesBurned().intValue() : 0)
                .sum();
    }

    @Transactional(readOnly = true)
    public int getTotalDurationByDateAndMember(LocalDate date, Member member) {
        return recordRepository.findAllByExerciseDateAndMember(date, member).stream()
                .mapToInt(record -> record.getDurationMinutes() != null ? record.getDurationMinutes().intValue() : 0)
                .sum();
    }
}
