package com.pathfinder.server.comment.controller;

import com.pathfinder.server.comment.dto.CommentDto;
import com.pathfinder.server.comment.entity.Comment;
import com.pathfinder.server.comment.mapper.CommentMapper;
import com.pathfinder.server.comment.service.CommentService;
import com.pathfinder.server.dto.MultiResponseDto;
import com.pathfinder.server.dto.SingleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper mapper;

    public CommentController(CommentService commentService, CommentMapper mapper) {
        this.commentService = commentService;
        this.mapper = mapper;
    }

    @PostMapping //댓글 생성
    public ResponseEntity postComment(@RequestBody CommentDto.Post commentPostDto){
        commentService.createComment(mapper.commentPostDtoToComment(commentPostDto));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/diary/{diary-id}") // 게시글에 따른 댓글 조회
    public ResponseEntity getComments(@PathVariable("diary-id") @Positive Long diaryId,
                                    @RequestParam int page) {
        Page<Comment> pageComments = commentService.getCommentsByDiaryId(diaryId, page-1);

        List<Comment> comments = pageComments.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(mapper.commentsToCommentResponseDtos(comments),
                        pageComments),
                HttpStatus.OK);
    }

    @PatchMapping("/{comment-id}")
    public ResponseEntity patchComment(@PathVariable("comment-id") @Positive Long commentId,
                                       @RequestBody CommentDto.Patch commentPatchDto) {

        commentPatchDto.setCommentId(commentId);
        Comment comment = commentService.updateComment(mapper.commentPatchDtoToComment(commentPatchDto));

        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.commentToCommentResponseDto(comment)),
                HttpStatus.OK);
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("comment-id") Long commentId){
        commentService.deleteComment(commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
