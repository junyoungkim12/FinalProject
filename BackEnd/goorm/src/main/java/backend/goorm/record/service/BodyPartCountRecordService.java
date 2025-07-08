package backend.goorm.record.service;

import backend.goorm.member.model.entity.Member;
import backend.goorm.record.entity.Record;
import backend.goorm.record.entity.WeeklyRecord;
import backend.goorm.record.repository.BodyPartCountRecordRepository;
import backend.goorm.record.repository.RecordRepository;
import backend.goorm.training.model.enums.TrainingCategoryType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class BodyPartCountRecordService {

    private final RecordRepository recordRepository;
    private final BodyPartCountRecordRepository bodyPartCountRecordRepository;

    /**
     * 여러 레코드에 대해 바디파트 기록을 생성 또는 갱신하는 프로세스
     */
    public int executeBodyPartCountRecordProcess(List<Record> records) {
        for (Record record : records) {
            this.saveOrUpdateBodyPartCountRecord(record);
        }
        return records.size();
    }

    /**
     * 단일 레코드 기준 바디파트 기록 생성/업데이트
     */
    private void saveOrUpdateBodyPartCountRecord(Record record) {
        LocalDate startDate = record.getExerciseDate().withDayOfMonth(1);
        LocalDate endDate = record.getExerciseDate().withDayOfMonth(record.getExerciseDate().lengthOfMonth());

        WeeklyRecord existingRecord = bodyPartCountRecordRepository.findAllByStartDateAndEndDate(startDate, endDate)
                .stream().findFirst().orElse(null);

        Map<TrainingCategoryType, Double> countMap = getCountMapByRecords(List.of(record));

        if (existingRecord != null) {
            setBodyPartCountRecordFieldsFromMap(existingRecord, countMap);
            bodyPartCountRecordRepository.save(existingRecord);
        } else {
            WeeklyRecord newRecord = WeeklyRecord.builder()
                    .startDate(startDate)
                    .endDate(endDate)
                    .build();
            setBodyPartCountRecordFieldsFromMap(newRecord, countMap);
            bodyPartCountRecordRepository.save(newRecord);
        }
    }

    /**
     * 바디파트별 운동 횟수/비율 반환
     */
    public Map<TrainingCategoryType, BodyPartCountInfo> getBodyPartCountInfoMap(List<Record> records) {
        Map<TrainingCategoryType, Double> countMap = getCountMapByRecords(records);
        double totalExercises = records.size();

        Map<TrainingCategoryType, BodyPartCountInfo> infoMap = new HashMap<>();
        for (Map.Entry<TrainingCategoryType, Double> entry : countMap.entrySet()) {
            double percentage = totalExercises > 0 ? (entry.getValue() / totalExercises) * 100 : 0;
            infoMap.put(entry.getKey(), new BodyPartCountInfo(entry.getValue(), percentage));
        }
        return infoMap;
    }

    /**
     * 주어진 레코드들에서 카테고리별 운동 횟수 집계
     */
    public Map<TrainingCategoryType, Double> getCountMapByRecords(List<Record> records) {
        Map<TrainingCategoryType, Double> countMap = new HashMap<>();
        for (Record record : records) {
            TrainingCategoryType category = record.getTraining().getCategory().getCategoryName();
            countMap.put(category, countMap.getOrDefault(category, 0.0) + 1);
        }
        return countMap;
    }

    /**
     * 날짜+회원 기준 레코드 리스트 조회 (쿼리 메서드 활용)
     */
    public List<Record> getRecordsByDate(LocalDate date, Member member) {
        // 권장: Repository에 쿼리 메서드 추가
        // List<Record> findByExerciseDateAndMember(LocalDate date, Member member);
        return recordRepository.findByExerciseDateAndMember(date, member);
    }

    /**
     * 기간+회원 기준 페이징 레코드 조회
     */
    public Page<Record> getRecordsByDateRange(LocalDate start, LocalDate end, Member member, Pageable pageable) {
        return recordRepository.findByExerciseDateBetweenAndMember(start, end, member, pageable);
    }

    /**
     * 카테고리 카운트 Map -> WeeklyRecord의 필드 매핑
     */
    private void setBodyPartCountRecordFieldsFromMap(WeeklyRecord weeklyRecord, Map<TrainingCategoryType, Double> countMap) {
        weeklyRecord.setCardio(countMap.getOrDefault(TrainingCategoryType.유산소, 0.0));
        weeklyRecord.setChest(countMap.getOrDefault(TrainingCategoryType.가슴, 0.0));
        weeklyRecord.setBack(countMap.getOrDefault(TrainingCategoryType.등, 0.0));
        weeklyRecord.setLegs(countMap.getOrDefault(TrainingCategoryType.하체, 0.0));
        weeklyRecord.setShoulder(countMap.getOrDefault(TrainingCategoryType.어깨, 0.0));
        weeklyRecord.setBiceps(countMap.getOrDefault(TrainingCategoryType.이두, 0.0));
        weeklyRecord.setTriceps(countMap.getOrDefault(TrainingCategoryType.삼두, 0.0));
        weeklyRecord.setAbs(countMap.getOrDefault(TrainingCategoryType.복근, 0.0));
        weeklyRecord.setEtc(countMap.getOrDefault(TrainingCategoryType.기타, 0.0));
    }

    /**
     * 바디파트별 카운트+퍼센트 데이터 객체
     */
    public static class BodyPartCountInfo {
        private final double count;
        private final double percentage;

        public BodyPartCountInfo(double count, double percentage) {
            this.count = count;
            this.percentage = percentage;
        }
        public double getCount() { return count; }
        public double getPercentage() { return percentage; }
    }
}
