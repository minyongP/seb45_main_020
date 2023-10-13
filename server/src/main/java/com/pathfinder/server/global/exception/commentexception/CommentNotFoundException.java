package com.pathfinder.server.global.exception.commentexception;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends CommentException{
    public static final String MESSAGE = "댓글을 찾을 수 없습니다.";
    public static final String CODE = "Comment-404";

    public CommentNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
