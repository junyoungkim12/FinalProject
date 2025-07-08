package backend.goorm.diet.entity;

import backend.goorm.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.ArrayList;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long foodId;

    @Column(name = "food_name", nullable = false)
    private String foodName;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Float gram;

    @Column(name = "calories")
    private Float calories;

    @Column(name = "carbohydrate")
    private Float carbohydrate;

    @Column(name = "protein")
    private Float protein;

    @Column(name = "fat")
    private Float fat;

    private Float sugar;
    private Float salt;
    private Float cholesterol;
    private Float saturatedFat;
    private Float transFat;

    @Column(name = "user_register", nullable = false)
    private Boolean userRegister = false;

    @Column(name = "use_count", nullable = false)
    private Integer useCount = 0;

    @OneToMany(mappedBy = "food")
    @Builder.Default
    private List<Diet> diets = new ArrayList<>();

    // 도메인 메서드: 사용 카운트 증가
    public void increaseUseCount() {
        this.useCount++;
    }

}
