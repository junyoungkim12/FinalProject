package backend.goorm.record.entity;

import backend.goorm.member.model.entity.Member;
import backend.goorm.training.model.enums.TrainingCategoryType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class WeeklyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private int trainingPerWeek;

    @Setter
    private LocalDate startDate;
    @Setter
    private LocalDate endDate;

    // 주간 body part count 기록
    @Setter
    private double cardio;
    @Setter
    private double chest;
    @Setter
    private double back;
    @Setter
    private double legs;
    @Setter
    private double shoulder;
    @Setter
    private double biceps;
    @Setter
    private double triceps;
    @Setter
    private double abs;
    @Setter
    private double etc;

    /**
     * 카테고리별로 해당 부위 카운트 누적
     */
    public void addCountValues(TrainingCategoryType category, double value) {
        switch (category) {
            case 유산소:
                this.cardio += value;
                break;
            case 가슴:
                this.chest += value;
                break;
            case 등:
                this.back += value;
                break;
            case 하체:
                this.legs += value;
                break;
            case 어깨:
                this.shoulder += value;
                break;
            case 이두:
                this.biceps += value;
                break;
            case 삼두:
                this.triceps += value;
                break;
            case 복근:
                this.abs += value;
                break;
            default:
                this.etc += value;
                break;
        }
    }
}

