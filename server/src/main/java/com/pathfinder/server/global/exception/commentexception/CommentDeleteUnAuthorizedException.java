package com.pathfinder.server.global.exception.commentexception;

import org.springframework.http.HttpStatus;

public class CommentDeleteUnAuthorizedException extends CommentException{
    public static final String MESSAGE = "삭제할 수 있는 권한이 없습니다.";
    public static final String CODE = "Comment-403";

    public CommentDeleteUnAuthorizedException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
