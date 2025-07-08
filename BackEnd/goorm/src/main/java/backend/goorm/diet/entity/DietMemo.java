package backend.goorm.diet.entity;

import backend.goorm.member.model.entity.Member;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "diet_memo")
public class DietMemo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long memoId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "dietMemo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Diet> diets = new ArrayList<>();

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Setter
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    // 도메인 메서드 (연관관계 관리)
    public void addDiet(Diet diet) {
        this.diets.add(diet);
        diet.setDietMemo(this);
    }
}

