package com.pathfinder.server.tourinfo.service;

import com.nimbusds.jose.util.Pair;
import com.pathfinder.server.tourinfo.entity.TourInfo;
import com.pathfinder.server.tourinfo.repository.TourInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TourInfoService {
    private final TourInfoRepository tourInfoRepository;
    private final TourInfoCities tourInfoCities;
    private final String API_KEY;
    private final RestTemplate restTemplate;

    public TourInfoService(@Value("${open-api.tour-api.credentials.service-key}") String apiKey, TourInfoRepository tourInfoRepository, TourInfoCities tourInfoCities, RestTemplate restTemplate) {
        API_KEY = apiKey;
        this.tourInfoRepository = tourInfoRepository;
        this.tourInfoCities = tourInfoCities;
        this.restTemplate = restTemplate;
    }

    @Transactional(readOnly = true)
    public Pair<String, List<TourInfo>> findTourInfosByAddress(String address) {
        String addr = address;
        if (addr.equals("랜덤")) {
            addr = tourInfoCities.address[(int) (Math.random() * tourInfoCities.address.length)];
        }
        List<TourInfo> results = tourInfoRepository.findByAddr1Containing(addr);

        Collections.shuffle(results);

        return Pair.of(addr, results.stream().limit(6).collect(Collectors.toList()));
    }

}
