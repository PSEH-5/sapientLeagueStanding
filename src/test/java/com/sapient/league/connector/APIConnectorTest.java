package com.sapient.league.connector;

import com.sapient.league.connector.config.APIConnectorConfig;
import com.sapient.league.connector.model.TeamStandingResponse;
import com.sapient.league.exception.DataNotFoundException;
import com.sapient.league.resource.model.filter.LeagueResourceFilter;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class APIConnectorTest {


    private APIConnector apiConnector;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private APIConnectorConfig apiConnectorConfig;

    private
    @BeforeEach
    void setUp() {
        apiConnector = new APIConnector(restTemplate,apiConnectorConfig);
    }

    @Test
    @DisplayName("Test to validate teamstanding response gets populated correctly")
    void getTeamStandings() {
        Map<String, String> params = new HashMap<>();
        TeamStandingResponse teamStandingResponse = TeamStandingResponse.builder().countryName("testCountry").leagueId("testLeagueId").leaguePosition("testLeaguePosition").teamId("teamId").teamName("testTeamName").build();
        TeamStandingResponse[] teamStandings = {teamStandingResponse};
        when(apiConnectorConfig.getApiEndPointHost()).thenReturn("http://localhost");
        when(apiConnectorConfig.getApiEndPointPath()).thenReturn("/test/api");
        when(restTemplate.exchange(any(String.class),any(HttpMethod.class),any(HttpEntity.class),eq(TeamStandingResponse[].class))).thenReturn(new ResponseEntity<>(teamStandings, HttpStatus.OK));
        LeagueResourceFilter leagueResourceFilter = LeagueResourceFilter.builder().queryParamMap(params).build();
        TeamStandingResponse[]  teamStandingResponses =apiConnector.getTeamStandings(leagueResourceFilter).get();
        assertTrue(teamStandingResponses.length ==1);
        assertEquals(teamStandingResponses[0].getCountryName(),"testCountry");
        assertEquals(teamStandingResponses[0].getLeagueId(),"testLeagueId");
        assertEquals(teamStandingResponses[0].getLeaguePosition(),"testLeaguePosition");
        assertEquals(teamStandingResponses[0].getTeamId(),"teamId");
        assertEquals(teamStandingResponses[0].getTeamName(),"testTeamName");


    }

    @Test
    @DisplayName("Test to validate exception type of 400 is captured")
    void validateDataNotFoundExceptionIfDownStreamThrows400(){
        Map<String, String> params = new HashMap<>();
        when(apiConnectorConfig.getApiEndPointHost()).thenReturn("http://localhost");
        when(apiConnectorConfig.getApiEndPointPath()).thenReturn("/test/api");
        when(restTemplate.exchange(any(String.class),any(HttpMethod.class),any(HttpEntity.class),eq(TeamStandingResponse[].class))).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        LeagueResourceFilter leagueResourceFilter = LeagueResourceFilter.builder().queryParamMap(params).build();
        Assertions.assertThrows(DataNotFoundException.class,()->{
            apiConnector.getTeamStandings(leagueResourceFilter);
        });

    }
}