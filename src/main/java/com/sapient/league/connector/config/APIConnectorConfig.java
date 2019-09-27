package com.sapient.league.connector.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class APIConnectorConfig {

    @Value("${apiServiceEndPointHost}")
    private String apiEndPointHost;

    @Value("${apiServiceEndPointPath}")
    private String apiEndPointPath;

    @Value("${apiKey}")
    private String apiKey;
}
