package backend.goorm.training.model.entity;

import backend.goorm.member.model.entity.Member;
import backend.goorm.record.entity.Record;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "training")
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_id")
    private Long trainingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private TrainingCategory category;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @Column(name = "user_register", nullable = false)
    private Boolean userCustom = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "calories_burned_per_minute")
    private Float caloriesBurnedPerMinute;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "training", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> trainingRecords = new ArrayList<>();

    @Builder
    public Training(TrainingCategory category, String trainingName, Boolean userCustom, Member member,
                    Float caloriesBurnedPerMinute, String imageUrl) {
        this.category = category;
        this.trainingName = trainingName;
        this.userCustom = userCustom;
        this.member = member;
        this.caloriesBurnedPerMinute = caloriesBurnedPerMinute;
        this.imageUrl = imageUrl;
    }


}
