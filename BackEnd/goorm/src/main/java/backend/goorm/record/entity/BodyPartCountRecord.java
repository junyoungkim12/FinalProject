package backend.goorm.record.entity;

import backend.goorm.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BodyPartCountRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRAINING_RECORD_ID")
    private TrainingRecord trainingRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDate date;

    private boolean weeklyRecordedYn;

    private double chest;
    private double back;
    private double legs;
    private double shoulder;
    private double biceps;
    private double triceps;
    private double abs;
    private double etc;

    @Builder
    public BodyPartCountRecord(TrainingRecord trainingRecord, Member member, LocalDate date, boolean weeklyRecordedYn,
                               double chest, double back, double legs, double shoulder, double biceps,
                               double triceps, double abs, double etc) {
        this.trainingRecord = trainingRecord;
        this.member = member;
        this.date = date;
        this.weeklyRecordedYn = weeklyRecordedYn;
        this.chest = chest;
        this.back = back;
        this.legs = legs;
        this.shoulder = shoulder;
        this.biceps = biceps;
        this.triceps = triceps;
        this.abs = abs;
        this.etc = etc;
    }

    // 업데이트 명확화: 상태 변경 메서드로 관리
    public void update(double chest, double back, double legs, double shoulder, double biceps,
                       double triceps, double abs, double etc) {
        this.chest = chest;
        this.back = back;
        this.legs = legs;
        this.shoulder = shoulder;
        this.biceps = biceps;
        this.triceps = triceps;
        this.abs = abs;
        this.etc = etc;
    }

    @Override
    public String toString() {
        return "BodyPartCountRecord{" +
                "id=" + id +
                ", date=" + date +
                ", chest=" + chest +
                ", back=" + back +
                ", legs=" + legs +
                ", shoulder=" + shoulder +
                ", biceps=" + biceps +
                ", triceps=" + triceps +
                ", abs=" + abs +
                ", etc=" + etc +
                '}';
    }
}
