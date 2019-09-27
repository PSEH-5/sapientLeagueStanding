package com.sapient.league.resource.model.filter;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class LeagueResourceFilter {

    private Map<String, String> queryParamMap;
}
