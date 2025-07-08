package backend.goorm.diet.dto;

import backend.goorm.diet.entity.DietMemo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DietMemoDto {
    private Long memoId;
    private Long memberId;
    private String content;
    private LocalDate date;

    public static DietMemoDto fromEntity(DietMemo memo) {
        return DietMemoDto.builder()
                .memoId(memo.getMemoId())
                .memberId(memo.getMember().getMemberId())
                .content(memo.getContent())
                .date(memo.getDate())
                .build();
    }
}
