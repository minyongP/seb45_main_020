package com.pathfinder.server.tourinfo.controller;

import com.nimbusds.jose.util.Pair;
import com.pathfinder.server.dto.SingleResponseDto;
import com.pathfinder.server.tourinfo.dto.TourInfoDto;
import com.pathfinder.server.tourinfo.entity.TourInfo;
import com.pathfinder.server.tourinfo.mapper.TourInfoMapper;
import com.pathfinder.server.tourinfo.service.TourInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TourInfoController {
    private final TourInfoService tourInfoService;
    private final TourInfoMapper mapper;

    public TourInfoController(TourInfoService tourInfoService, TourInfoMapper mapper) {
        this.tourInfoService = tourInfoService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity getTourInfoList(@RequestParam String address) {
        Pair<String, List<TourInfo>> result =  tourInfoService.findTourInfosByAddress(address);
        String addr = result.getLeft();
        List<TourInfo> tourInfos =  result.getRight();

        List<TourInfoDto.Responses> responses = mapper.tourInfoToTourInfoResponses(tourInfos);
        TourInfoDto tourInfoDto = new TourInfoDto(responses, addr);

        return new ResponseEntity<>(
                new SingleResponseDto<>(tourInfoDto),
                HttpStatus.OK
        );
    }

    @GetMapping("{content-id}")
    public ResponseEntity getTourInfo(@PathVariable("content-id") @Positive long contentId) {
        TourInfo tourInfo = tourInfoService.findTourInfo(contentId);
        return new ResponseEntity<>(
                new SingleResponseDto<>(mapper.tourInfoToTourInfoResponse(tourInfo))
                , HttpStatus.OK
        );
    }

}
