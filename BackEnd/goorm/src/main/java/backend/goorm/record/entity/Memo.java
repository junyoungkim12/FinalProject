package backend.goorm.record.entity;


import backend.goorm.member.model.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "memo")
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long memoId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Setter
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Setter
    @Column(name = "content", nullable = false)
    private String content;

    @Setter
    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records = new ArrayList<>();

    @Builder
    public Memo(Member member, LocalDate date, String content) {
        this.member = member;
        this.date = date;
        this.content = content;
    }

    // 연관관계 편의 메서드
    public void addRecord(Record record) {
        records.add(record);
        record.setMemo(this);
    }
}
