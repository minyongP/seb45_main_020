package com.pathfinder.server.comment.mapper;

import com.pathfinder.server.comment.dto.CommentDto;
import com.pathfinder.server.comment.entity.Comment;
import com.pathfinder.server.diary.entity.Diary;
import com.pathfinder.server.member.entity.Member;
import com.pathfinder.server.scrap.dto.ScrapDto;
import com.pathfinder.server.scrap.entity.Scrap;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
//    Comment commentPostDtoToComment(CommentDto.Post commentPostDto);

    Comment commentPatchDtoToComment(CommentDto.Patch commentPatchDto);

    CommentDto.Response commentToCommentResponseDto(Comment comment);

    List<CommentDto.Response> commentsToCommentResponseDtos(List<Comment> comments);

    default Comment commentPostDtoToComment(CommentDto.Post commentPostDto){
        Comment comment = new Comment();
        Diary diary = new Diary();
        Member member = new Member();

        member.setMemberId(commentPostDto.getMemberId());
        comment.setMember(member);
        diary.setDiaryId(commentPostDto.getDiaryId());
        comment.setDiary(diary);
        comment.setContent(commentPostDto.getContent());



        return comment;
    }
}
