package com.pathfinder.server.tourinfo.mapper;

import com.pathfinder.server.tourinfo.dto.TourInfoDto;
import com.pathfinder.server.tourinfo.entity.TourInfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TourInfoMapper {
    TourInfoDto.Response tourInfoToTourInfoResponse(TourInfo tourInfo);
    List<TourInfoDto.Responses> tourInfoToTourInfoResponses(List<TourInfo> tourInfos);
}
