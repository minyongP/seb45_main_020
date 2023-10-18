package com.pathfinder.server.diary.service;

import com.pathfinder.server.auth.utils.SecurityUtil;
import com.pathfinder.server.diary.entity.Diary;
import com.pathfinder.server.diary.repository.DiaryRepository;
import com.pathfinder.server.global.exception.diaryexception.DiaryDeleteUnAuthorizedException;
import com.pathfinder.server.global.exception.diaryexception.DiaryEditUnAuthorizedException;
import com.pathfinder.server.global.exception.diaryexception.DiaryNotFoundException;
import com.pathfinder.server.member.entity.Authority;
import com.pathfinder.server.member.entity.Member;
import com.pathfinder.server.member.service.MemberService;
import com.pathfinder.server.reward.service.RewardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final MemberService memberService;
    private final RewardService rewardService;

    public DiaryService(DiaryRepository diaryRepository, MemberService memberService, RewardService rewardService) {
        this.diaryRepository = diaryRepository;
        this.memberService = memberService;
        this.rewardService = rewardService;
    }

    public Diary createDiary(Diary diary) {
        verifyDiaryGetMemberName(diary);
        diary.setCreatedAt(LocalDateTime.now());
        diary.setModifiedAt(LocalDateTime.now());

        return diaryRepository.save(diary);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Diary updateDiary(Diary diary){
        Diary findDiary = findVerifiedDiary(diary.getDiaryId());
        if(verifyIdentification(findDiary)){
            Optional.ofNullable(diary.getTitle())
                    .ifPresent(title -> findDiary.setTitle(title));
            Optional.ofNullable(diary.getContent())
                    .ifPresent(content -> findDiary.setContent(content));
            Optional.ofNullable(diary.getArea1())
                    .ifPresent(area1 -> findDiary.setArea1(area1));
            Optional.ofNullable(diary.getArea2())
                    .ifPresent(area2 -> findDiary.setArea2(area2));

            findDiary.setModifiedAt(LocalDateTime.now());
            return diaryRepository.save(findDiary);
        }
        else {
            throw new DiaryEditUnAuthorizedException();
        }
    }

    @Transactional
    public Diary getDiary(Long diaryId){
        Diary findDiary = findVerifiedDiary(diaryId);
        findDiary.setViews(findDiary.getViews() + 1); // 조회수 증가
        findDiary.setRecommendedCount(findDiary.getRecommends().stream().count());

        return diaryRepository.save(findDiary);
    }

    public Page<Diary> getDiaries(int page){
        return diaryRepository.findAll(PageRequest.of(page, 10, Sort.by("diaryId").descending()));
    }

    public Page<Diary> getDiariesByRegion(String area1, int page){
        return diaryRepository.findByArea1(area1, PageRequest.of(page - 1,10, Sort.by("diaryId").descending()));
    }

    public Page<Diary> getDiariesByMember(Long memberId, int page){
        return diaryRepository.findByMemberMemberId(memberId, PageRequest.of(page - 1,10, Sort.by("diaryId").descending()));
    }

    public Page<Diary> getTop3DiariesByRecommendedCountForDay() { // 일별 조회
        Pageable pageable = PageRequest.of(0, 3);
        LocalDate day = LocalDate.now();
        Date date = Date.from(day.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return diaryRepository.findByTop3ByOrderedByRecommendedCountForDay(date,pageable);
    }

    public Page<Diary> getTop3DiariesByRecommendedCountForWeek() { // 주별 조회
        Pageable pageable = PageRequest.of(0, 3);
        int year = LocalDate.now().getYear();
        int weekOfYear = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        int week = year * 100 + weekOfYear;

        return diaryRepository.findByTop3ByOrderedByRecommendedCountForWeek(week, pageable);
    }

    public Page<Diary> getTop3DiariesByRecommendedCountForMonth() { // 월별 조회
        Pageable pageable = PageRequest.of(0, 3);
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        return diaryRepository.findByTop3ByOrderedByRecommendedCountForMonth(year,month,pageable);
    }

    public Page<Diary> getTop3DiariesByRecommendedCountForYear() { // 연별 조회
        Pageable pageable = PageRequest.of(0, 3);
        int year = LocalDate.now().getYear();
        return diaryRepository.findByTop3ByOrderedByRecommendedCountForYear(year,pageable);
    }

    public void deleteDiary(Long diaryId) {
        Diary findDiary = findVerifiedDiary(diaryId);
        if(verifyIdentification(findDiary)){
            diaryRepository.delete(findDiary);
        }
        else {
            throw new DiaryDeleteUnAuthorizedException();
        }
    }
    @Transactional
    public Diary findVerifiedDiary(Long diaryId) {
        Optional<Diary> optionalQuestion = diaryRepository.findById(diaryId);
        Diary findDiary = optionalQuestion.orElseThrow(()-> new DiaryNotFoundException());
        return findDiary;
    }
    private void verifyDiaryGetMemberName(Diary diary){
        Member findUser = memberService.findVerifiedMember(diary.getMember().getMemberId());
        diary.setName(findUser.getName());
        diary.setEmail(findUser.getEmail());
        findUser.setDiaryCount(findUser.getDiaryCount() + 1);
        rewardService.unlockRewards(findUser,findUser.getRewards());
    }
    public boolean verifyIdentification(Diary diary) {
        if((diary.getMember().getMemberId() == SecurityUtil.getCurrentId()) || (SecurityUtil.getAuthority() == Authority.ROLE_ADMIN)) {
            return true;
        }
        return false;
    }

}
