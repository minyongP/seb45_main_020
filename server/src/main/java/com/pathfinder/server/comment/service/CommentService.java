package com.pathfinder.server.comment.service;

import com.pathfinder.server.auth.utils.SecurityUtil;
import com.pathfinder.server.comment.entity.Comment;
import com.pathfinder.server.comment.repository.CommentRepository;
import com.pathfinder.server.diary.entity.Diary;
import com.pathfinder.server.diary.service.DiaryService;
import com.pathfinder.server.global.exception.commentexception.CommentDeleteUnAuthorizedException;
import com.pathfinder.server.global.exception.commentexception.CommentEditUnAuthorizedException;
import com.pathfinder.server.global.exception.commentexception.CommentNotFoundException;
import com.pathfinder.server.global.exception.diaryexception.DiaryEditUnAuthorizedException;
import com.pathfinder.server.member.entity.Authority;
import com.pathfinder.server.member.entity.Member;
import com.pathfinder.server.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {

    private final DiaryService diaryService;
    private final CommentRepository commentRepository;
    private final MemberService memberService;

    public CommentService(DiaryService diaryService, CommentRepository commentRepository, MemberService memberService) {
        this.diaryService = diaryService;
        this.commentRepository = commentRepository;
        this.memberService = memberService;
    }

    public Comment createComment(Comment comment) {
        Diary findDiary = diaryService.findVerifiedDiary(comment.getDiary().getDiaryId());
        Member findMember = memberService.findVerifiedMember(comment.getMember().getMemberId());
        comment.setName(findMember.getName());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setModifiedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Comment updateComment(Comment comment){
        Comment findComment = findVerifiedComment(comment.getCommentId());
        if(verifyIdentification(findComment)){
            Optional.ofNullable(comment.getContent())
                    .ifPresent(content -> findComment.setContent(content));

            findComment.setModifiedAt(LocalDateTime.now());
            return commentRepository.save(findComment);
        }
        else {
            throw new CommentEditUnAuthorizedException();
        }
    }
    public Page<Comment> getCommentsByDiaryId(Long diaryId,int page){
        return commentRepository.findByDiaryDiaryId(diaryId,PageRequest.of(page, 10, Sort.by("commentId").descending()));
    }

    public void deleteComment(Long commentId) {
        Comment findComment = findVerifiedComment(commentId);
        if(verifyIdentification(findComment)){
            commentRepository.delete(findComment);
        }
        else {
            throw new CommentDeleteUnAuthorizedException();
        }
    }

    @Transactional
    public Comment findVerifiedComment(Long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        Comment findComment = optionalComment.orElseThrow(()-> new CommentNotFoundException());
        return findComment;
    }

    private void verifyCommentGetMemberName(Comment comment){
        Member findMember = memberService.findVerifiedMember(comment.getMember().getMemberId());
        comment.setName(findMember.getName());
        comment.getDiary().setCommentCount(comment.getDiary().getCommentCount()+1);
    }
    public boolean verifyIdentification(Comment comment) {
        if((comment.getMember().getMemberId() == SecurityUtil.getCurrentId()) || (SecurityUtil.getAuthority() == Authority.ROLE_ADMIN)) {
            return true;
        }
        return false;
    }
}
