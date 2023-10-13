package com.pathfinder.server.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class CommentDto {
    @Getter
    public static class Post {
        private Long diaryId;
        private Long memberId;
        private String content;
    }

    @Getter
    public static class Patch {
        private Long commentId;
        private String content;

        public void setCommentId(Long commentId) {
            this.commentId = commentId;
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private Long commentId;
        private String name;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }
}
