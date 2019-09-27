package com.sapient.league.service;

import com.sapient.league.connector.APIConnector;
import com.sapient.league.connector.model.TeamStandingResponse;
import com.sapient.league.exception.DataNotFoundException;
import com.sapient.league.resource.model.filter.LeagueResourceFilter;
import com.sapient.league.resource.model.filter.TeamStanding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LeagueStandingService {


    private APIConnector apiConnector;

    @Autowired
    public LeagueStandingService(APIConnector apiConnector) {

        this.apiConnector = apiConnector;

    }

    public List<TeamStanding> getTeamStanding(LeagueResourceFilter leagueResourceFilter) {
        TeamStandingResponse[] teamStandingResponses = apiConnector.getTeamStandings(leagueResourceFilter).orElseThrow(() -> new DataNotFoundException("Unable to find requested resource", "API_400_001"));
        List<TeamStanding> teamStandings = Stream.of(teamStandingResponses).map(
                str -> TeamStanding.builder().
                        countryName(str.getCountryName())
                        .leagueId(str.getLeagueId())
                        .leagueName(str.getLeagueName())
                        .teamId(str.getTeamId())
                        .teamName(str.getTeamName())
                        .leaguePosition(str.getLeaguePosition())
                        .build()).collect(Collectors.toList());
        return teamStandings;
    }
}
