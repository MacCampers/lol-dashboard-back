package com.loldashboard.loldashboard.services;

import com.loldashboard.loldashboard.models.AccountDto;
import com.loldashboard.loldashboard.models.ChampionMasteryDto;
import com.loldashboard.loldashboard.models.SummonerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RiotAPIService {

    @Value("${riot.api.key}")
    private String riotApiKey;

    private final String RIOT_EU_API = "https://europe.api.riotgames.com";
    private final String RIOT_NA_API = "https://americas.api.riotgames.com";
    private final String RIOT_ASIA_API = "https://asia.api.riotgames.com";
    private final String RIOT_EUW1_API = "https://euw1.api.riotgames.com";

    private final RestTemplate restTemplate;

    public RiotAPIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<AccountDto> getAccountInformation(final String gameName, final String tagLine) {
        String url = RIOT_EU_API + "/riot/account/v1/accounts/by-riot-id";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("gameName", gameName);
        queryParams.put("tagLine", tagLine);
        return callRiotApi(url, queryParams, AccountDto.class);
    }

    public ResponseEntity<SummonerDTO> getAccountDetails(final String encryptedPUUID) {
        String url = RIOT_EUW1_API + "/lol/summoner/v4/summoners/by-puuid/";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("puuid", encryptedPUUID);
        return callRiotApi(url, queryParams, SummonerDTO.class);
    }


    public ResponseEntity<Integer> getTotalMasteriesByPuuid(final String encryptedPUUID) {
        String url = RIOT_EUW1_API + "/lol/champion-mastery/v4/scores/by-puuid/";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("puuid", encryptedPUUID);
        return callRiotApi(url, queryParams, Integer.class);
    }

    public ResponseEntity<List<ChampionMasteryDto>> getAllMasteriesByPuuid(final String encryptedPUUID) {
        String url = RIOT_EUW1_API + "/lol/champion-mastery/v4/champion-masteries/by-puuid";
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("puuid", encryptedPUUID);
        return callRiotApiForList(url, queryParams, ChampionMasteryDto.class);
    }

    public <T> ResponseEntity<T> callRiotApi(String url, Map<String, String> queryParams, Class<T> responseType) {
        // Build the URL with path variables
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            url += "/" + entry.getValue();
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey);
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
        headers.set("Accept-Language", "fr-FR,fr;q=0.5");
        headers.set("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");


        return restTemplate.exchange(
                builder.build().toUri(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                responseType
        );

    }
    public <T> ResponseEntity<List<T>> callRiotApiForList(String url, Map<String, String> queryParams, Class<T> responseType) {
        // Build the URL with path variables
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            url += "/" + entry.getValue();
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

        // Set the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Riot-Token", riotApiKey);
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36");
        headers.set("Accept-Language", "fr-FR,fr;q=0.5");
        headers.set("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");

        // Make the request with RestTemplate.exchange method
        ResponseEntity<List<T>> responseEntity = restTemplate.exchange(
                builder.build().toUri(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<T>>() {}
        );

        return responseEntity;
    }
}