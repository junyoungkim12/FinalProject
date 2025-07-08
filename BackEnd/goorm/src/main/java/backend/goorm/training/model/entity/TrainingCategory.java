package backend.goorm.training.model.entity;

import backend.goorm.training.model.enums.TrainingCategoryType;
import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "training_category")
public class TrainingCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_name", nullable = false)
    private TrainingCategoryType categoryName;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Training> trainings = new ArrayList<>();

    @Builder
    public TrainingCategory(TrainingCategoryType categoryName) {
        this.categoryName = categoryName;
    }


}

