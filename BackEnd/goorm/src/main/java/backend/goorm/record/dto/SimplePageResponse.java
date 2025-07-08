package backend.goorm.record.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimplePageResponse<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
}