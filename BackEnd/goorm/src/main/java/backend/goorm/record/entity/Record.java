package backend.goorm.record.entity;

import backend.goorm.member.model.entity.Member;
import backend.goorm.training.model.entity.Training;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "record_date", nullable = false)
    private LocalDateTime recordDate;

    @Column(name = "exercise_date", nullable = false)
    private LocalDate exerciseDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "calories_burned", nullable = false)
    private Float caloriesBurned;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "intensity")
    private String intensity;

    @Column(name = "sets")
    private Integer sets;

    @Column(name = "reps")
    private Integer reps;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "distance")
    private Float distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @Column(name = "satisfaction")
    private Integer satisfaction;

    @Builder
    public Record(Training training, Member member, LocalDateTime recordDate, LocalDate exerciseDate,
                  Float caloriesBurned, Integer durationMinutes, String intensity,
                  Integer sets, Integer reps, Integer weight, Float distance, Integer satisfaction) {
        this.training = training;
        this.member = member;
        this.recordDate = recordDate;
        this.exerciseDate = exerciseDate;
        this.caloriesBurned = caloriesBurned;
        this.durationMinutes = durationMinutes;
        this.intensity = intensity;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.distance = distance;
        this.satisfaction = satisfaction;
    }

    public void setMemo(Memo memo) {
        this.memo = memo;
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = LocalDateTime.now();
    }
}

