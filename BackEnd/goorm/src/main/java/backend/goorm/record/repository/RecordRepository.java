package backend.goorm.record.repository;


import backend.goorm.member.model.entity.Member;
import backend.goorm.record.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAll();
    List<Record> findAllByMember(Member member);
    Page<Record> findAllByMember(Member member, Pageable pageable);

    Page<Record> findByExerciseDateBetweenAndMember(LocalDate start, LocalDate end, Member member, Pageable pageable);

    List<Record> findAllByExerciseDateAndMember(LocalDate date, Member member);
    List<Record> findByExerciseDateAndMember(LocalDate date, Member member);


    @Query("SELECT r FROM Record r " +
            "JOIN FETCH r.training t " +
            "JOIN FETCH t.category c " +
            "WHERE r.recordId IN :recordIds")
    List<Record> findRecordsWithTrainingAndCategoryByRecordIds(List<Long> recordIds);


}
