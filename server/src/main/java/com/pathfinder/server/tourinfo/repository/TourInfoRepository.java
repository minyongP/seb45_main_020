package com.pathfinder.server.tourinfo.repository;

import com.pathfinder.server.tourinfo.entity.TourInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TourInfoRepository extends JpaRepository<TourInfo, Long> {
    Optional<TourInfo> findByContentId(long contentId);
    List<TourInfo> findByAddr1Containing(String addr1);
}
