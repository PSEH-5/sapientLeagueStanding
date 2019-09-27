package com.sapient.league.resource.model.filter;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class TeamStanding {

    private String countryName;

    private String leagueId;

    private String leagueName;

    private String teamId;

    private String teamName;

    private String leaguePosition;

}
