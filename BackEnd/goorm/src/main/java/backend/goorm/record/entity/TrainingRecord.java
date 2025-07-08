package backend.goorm.record.entity;

import backend.goorm.training.model.entity.Training;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "training_record")
public class TrainingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_record_id")
    private Long trainingRecordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private Record record;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @Builder
    public TrainingRecord(Record record, Training training) {
        this.record = record;
        this.training = training;
    }

//    // 연관관계 편의 메서드
//    public void setRecord(Record record) {
//        this.record = record;
//    }
//
//    public void setTraining(Training training) {
//        this.training = training;
//    }
}
