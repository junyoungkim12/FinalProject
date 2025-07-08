package backend.goorm.record.service;

import backend.goorm.member.model.entity.Member;
import backend.goorm.record.dto.MemoDto;
import backend.goorm.record.entity.Memo;
import backend.goorm.record.entity.Record;
import backend.goorm.record.repository.MemoRepository;
import backend.goorm.record.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final RecordRepository recordRepository;

    public MemoDto addOrUpdateMemo(MemoDto memoDto, Member member) {
        LocalDate date = memoDto.getDate();
        List<Record> records = recordRepository.findAllByExerciseDateAndMember(date, member);

        Memo memo = memoRepository.findByMemberAndDate(member, date)
                .orElseGet(() -> Memo.builder().member(member).date(date).build());

        memo.setMember(member);
        memo.setDate(date);
        memo.setContent(memoDto.getContent());
        memo.setRecords(records);

        memoRepository.save(memo);

        // Optional: log.info("메모 저장: member={}, date={}", member.getMemberId(), date);

        return MemoDto.fromEntity(memo);
    }

    public MemoDto getMemoByDateAndMember(LocalDate date, Member member) {
        Memo memo = memoRepository.findByMemberAndDate(member, date)
                .orElseThrow(() -> new IllegalArgumentException("해당 날짜의 메모를 찾지 못했습니다: " + date));
        return MemoDto.fromEntity(memo);
    }
}

