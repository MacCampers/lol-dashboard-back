package com.loldashboard.loldashboard.controllers;

import com.loldashboard.loldashboard.models.AccountDto;
import com.loldashboard.loldashboard.models.SummonerDTO;
import com.loldashboard.loldashboard.services.RiotAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private final RiotAPIService riotAPIService;

    public AccountController(RiotAPIService riotAPIService) {
        this.riotAPIService = riotAPIService;
    }

    @GetMapping("/getByUserTag/{gameName}/{tagLine}")
    public AccountDto getAccountInformation(@PathVariable("gameName") String gameName, @PathVariable("tagLine") String tagLine) {
        ResponseEntity<AccountDto> response = riotAPIService.getAccountInformation(gameName, tagLine);
        if (response.getStatusCode() == HttpStatus.OK) {
            AccountDto accountDto = response.getBody();
            if (accountDto != null) {
                System.out.println("Champion information: " + accountDto);
                return accountDto;
            } else {
                System.out.println("No account information found for gameName: " + gameName + " and tagLine: " + tagLine);
            }
        } else {
            System.out.println("Failed to retrieve account information. Status code: " + response.getStatusCodeValue());
        }
        return null;
    }

    @GetMapping("/getByPuuid/{encryptedPUUID}")
    public SummonerDTO getAccountDetails(@PathVariable("encryptedPUUID") String encryptedPUUID) {
        ResponseEntity<SummonerDTO> response = riotAPIService.getAccountDetails(encryptedPUUID);
        if (response.getStatusCode() == HttpStatus.OK) {
            SummonerDTO dto = response.getBody();
            if (dto != null) {
                System.out.println("account information: " + dto);
                return dto;
            } else {
                System.out.println("No account information found for encryptedPUUID: " + encryptedPUUID);
            }
        } else {
            System.out.println("Failed to retrieve account information. Status code: " + response.getStatusCodeValue());
        }
        return null;
    }
}
