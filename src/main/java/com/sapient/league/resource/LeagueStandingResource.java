package com.sapient.league.resource;

import com.sapient.league.resource.model.filter.LeagueResourceFilter;
import com.sapient.league.resource.model.out.TeamStanding;
import com.sapient.league.service.LeagueStandingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LeagueStandingResource {


    private LeagueStandingService leagueStandingService;

    @Autowired
    public LeagueStandingResource(LeagueStandingService leagueStandingService) {

        this.leagueStandingService = leagueStandingService;
    }
    @GetMapping(path = "/getstandings", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<List<TeamStanding>> getTeamStandings(@RequestParam(value = "countryName",required = false)String countryName,
                                                               @RequestParam(value = "leagueName",required = false)String leagueName, @RequestParam(value = "teamName",required = false)String teamName
    ) {
        Map<String,String> queryMap = new HashMap<>();
        queryMap.put("countryName",countryName);
        queryMap.put("leagueName",leagueName);
        queryMap.put("teamName",teamName);
        queryMap.put("league_id","148");
        queryMap.put("action","get_standings");

        LeagueResourceFilter filter = LeagueResourceFilter.builder().queryParamMap(queryMap).build();

        return new ResponseEntity<List<TeamStanding>>(leagueStandingService.getTeamStanding(filter), HttpStatus.OK);

    }

}
