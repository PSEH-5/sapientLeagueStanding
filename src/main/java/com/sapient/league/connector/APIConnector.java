package com.sapient.league.connector;

import com.sapient.league.connector.config.APIConnectorConfig;
import com.sapient.league.connector.model.TeamStandingResponse;
import com.sapient.league.exception.DataNotFoundException;
import com.sapient.league.resource.model.filter.LeagueResourceFilter;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class APIConnector {

    private RestTemplate restTemplate;

    private APIConnectorConfig apiConnectorConfig;

    @Autowired
    public APIConnector(RestTemplate restTemplate, APIConnectorConfig apiConnectorConfig) {
        this.restTemplate = restTemplate;
        this.apiConnectorConfig = apiConnectorConfig;

    }

    public Optional<TeamStandingResponse[]> getTeamStandings(LeagueResourceFilter leagueResourceFilter) {
        try {
            Map<String, String> queryMap = leagueResourceFilter.getQueryParamMap();
            queryMap.put("APIkey", apiConnectorConfig.getApiKey());
            HttpEntity httpEntity = new HttpEntity(null, getHeaders());
            return Optional.of(restTemplate.exchange(getTeamStandingURI(queryMap), HttpMethod.GET, null, TeamStandingResponse[].class).getBody());
        } catch (HttpClientErrorException clientEx) {
            if (clientEx.getStatusCode() == HttpStatus.BAD_REQUEST || clientEx.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new DataNotFoundException("Could not find requested resource", "API_400_001");
            }
            throw clientEx;
        }
    }

    private MultiValueMap<String, String> getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("ContentType", "application/json");
        return headers;
    }

    private String getTeamStandingURI(Map<String, String> params) {
        String uri = apiConnectorConfig.getApiEndPointHost();
        List<BasicNameValuePair> nameValuePairs = params.entrySet().stream().filter(entry->entry.getValue()!=null)
                .map(entry -> new BasicNameValuePair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        String urlParams = URLEncodedUtils.format(nameValuePairs, Charset.forName("UTF-8"));
        if (urlParams.isEmpty()) {
            return uri;
        }
        return uri + "?" + urlParams;
    }

}
