package com.pathfinder.server.global.exception.tourInfoexception;

import org.springframework.http.HttpStatus;

public class TourInfoNotFoundException extends TourInfoException{
    public static final String MESSAGE = "존재하지 않는 투어정보입니다.";
    public static final String CODE = "TourInfo-404";

    public TourInfoNotFoundException() {
        super(CODE, HttpStatus.NOT_FOUND, MESSAGE);
    }
}
