package com.pathfinder.server.global.exception.tourInfoexception;

import com.pathfinder.server.global.exception.BusinessException;
import org.springframework.http.HttpStatus;

public abstract class TourInfoException extends BusinessException {

    protected TourInfoException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
