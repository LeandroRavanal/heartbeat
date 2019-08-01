package io.github.lr.heartbeat.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import io.github.lr.heartbeat.managers.HeartbeatManager;

@Service
public class HeartbeatService {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String HEARTBEAT_PATH_REQUEST = "/api/v1/heartbeat";

	@Autowired private RestTemplate heartbeatRestTemplate; 
	
	private final String destination;
	
	public HeartbeatService(HeartbeatManager heartbeatManager, @Value("${heartbeat.master.origin}") String originMaster, @Value("${heartbeat.slave.origin}") String originSlave) {
		destination = (heartbeatManager.isInitialModeMaster() ? originSlave : originMaster) + HEARTBEAT_PATH_REQUEST;
		
		logger.info("Heartbeat destination: {}", destination);
	}
	
	public boolean heartbeat() {
		String errorMessage;
		
		try {
			ResponseEntity<String> response = heartbeatRestTemplate.exchange(destination, HttpMethod.HEAD, null, String.class);
			
			if (response.getStatusCode().is2xxSuccessful()) {
				logger.debug("Heartbeat response OK");
				
				return true;
			}
			
			errorMessage = response.getStatusCode().toString();
		
		} catch (RestClientException rce) {
			
			errorMessage = rce.getMessage();
		}
		
		logger.info("Heartbeat response error: {}", errorMessage);
		
		return false;
	}
	
}
