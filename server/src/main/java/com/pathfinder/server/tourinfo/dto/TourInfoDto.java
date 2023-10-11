package com.pathfinder.server.tourinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class TourInfoDto {
    private List<Responses> tourInfos;
    private String addr;

    @Getter
    @AllArgsConstructor
    public static class Responses {
        private Long contentId;
        private String title;
        private String addr1;
        private String addr2;
        private String firstimage;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long contentId;
        private String title;
        private String addr1;
        private String addr2;
        private String tel;
        private String firstimage;
        private String firstimage2;
        private String tag;
        private String zipcode;
    }
}