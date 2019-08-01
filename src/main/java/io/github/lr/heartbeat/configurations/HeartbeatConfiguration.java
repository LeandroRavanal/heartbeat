package io.github.lr.heartbeat.configurations;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.github.lr.heartbeat.modes.HeartbeatMasterMode;
import io.github.lr.heartbeat.modes.HeartbeatSlaveMode;

@Configuration
public class HeartbeatConfiguration {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Bean
    public RestTemplate heartbeatRestTemplate(RestTemplateBuilder restTemplateBuilder, @Value("${heartbeat.requestTimeoutMillis}") int requestTimeoutMillis)  {
    	final int defaultMinRequestTimeoutMillis = 10;
    	final int defaultMaxRequestTimeoutMillis = 100;
    	
    	if (requestTimeoutMillis < defaultMinRequestTimeoutMillis) {
    		
    		logger.warn("Invalid heartbeat request timeout {}, lower than {} millis.", requestTimeoutMillis, defaultMinRequestTimeoutMillis);
    		
    		requestTimeoutMillis = defaultMinRequestTimeoutMillis;
    	} else if (requestTimeoutMillis > defaultMaxRequestTimeoutMillis) {
    		
    		logger.warn("Invalid heartbeat request timeout {}, greater than {} millis", requestTimeoutMillis, defaultMaxRequestTimeoutMillis);    		
    		
    		requestTimeoutMillis = defaultMaxRequestTimeoutMillis;
    	}
    	
    	logger.info("Heartbeat request timeout: {} millis", requestTimeoutMillis);
    	
    	Duration timeout = Duration.ofMillis(requestTimeoutMillis);
    	
    	return restTemplateBuilder.setConnectTimeout(timeout).setReadTimeout(timeout).build();
    }
    
    @Bean
    @ConditionalOnMissingBean
    public HeartbeatMasterMode heartbeatMasterMode() {
    	return new HeartbeatMasterMode() {};
    }

    @Bean
    @ConditionalOnMissingBean
    public HeartbeatSlaveMode heartbeatSlaveMode() {
    	return new HeartbeatSlaveMode() {};
    }
	
}
