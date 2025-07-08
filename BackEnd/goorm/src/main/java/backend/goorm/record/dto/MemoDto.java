package backend.goorm.record.dto;

import backend.goorm.record.entity.Memo;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemoDto {
    private String content;
    private LocalDate date;

    public static MemoDto fromEntity(Memo memo) {
        return MemoDto.builder()
                .content(memo.getContent())
                .date(memo.getDate())
                .build();
    }
}
