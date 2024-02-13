package com.loldashboard.loldashboard.controllers;


import com.loldashboard.loldashboard.models.ChampionMasteryDto;
import com.loldashboard.loldashboard.services.RiotAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mastery")
public class MasteryController {

    @Autowired
    private final RiotAPIService riotAPIService;

    public MasteryController(RiotAPIService riotAPIService) {
        this.riotAPIService = riotAPIService;
    }

    @GetMapping("/getAll/{puuid}")
    public List<ChampionMasteryDto> getAllChampionMasteryScore(@PathVariable("puuid") String encryptedPUUID) {
        ResponseEntity<List<ChampionMasteryDto>> response = riotAPIService.getAllMasteriesByPuuid(encryptedPUUID);
        if (response.getStatusCode() == HttpStatus.OK) {
            List<ChampionMasteryDto> allMastery = response.getBody();
            if (allMastery != null) {
                System.out.println("Champion information: " + allMastery);
                return allMastery;
            } else {
                System.out.println("No mastery information found for encryptedPUUID" + encryptedPUUID);
            }
        } else {
            System.out.println("Failed to retrieve mastery information. Status code: " + response.getStatusCodeValue());
        }
        return null;
    }

    @GetMapping("/getTotal/{puuid}")
    public int getMasteryTotalScore(@PathVariable("puuid") String encryptedPUUID) {
        ResponseEntity<Integer> response = riotAPIService.getTotalMasteriesByPuuid(encryptedPUUID);
        if (response.getStatusCode() == HttpStatus.OK) {
            int totalScore = response.getBody();
            if (totalScore > 0) {
                System.out.println("Total score: " + totalScore);
                return totalScore;
            } else {
                System.out.println("No total score found for encryptedPUUID" + encryptedPUUID);
            }
        } else {
            System.out.println("Failed to retrieve  total score. Status code: " + response.getStatusCodeValue());
        }
        return 0;
    }
}