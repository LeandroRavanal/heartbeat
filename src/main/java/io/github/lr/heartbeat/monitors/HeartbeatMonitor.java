package io.github.lr.heartbeat.monitors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.github.lr.heartbeat.managers.HeartbeatManager;

@Component
public class HeartbeatMonitor {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
	private final int requestIntervalMillis;

	@Autowired private HeartbeatManager heartbeatManager; 
	
	public HeartbeatMonitor(@Value("${heartbeat.requestIntervalMillis}") int requestIntervalMillis) {
    	final int defaultMinRequestIntervalMillis = 10;
    	final int defaultMaxRequestIntervalMillis = 1000;
    	
    	if (requestIntervalMillis < defaultMinRequestIntervalMillis) {
    		
    		logger.warn("Invalid heartbeat request interval {}, lower than {} millis.", requestIntervalMillis, defaultMinRequestIntervalMillis);
    		
    		requestIntervalMillis = defaultMinRequestIntervalMillis;
    	} else if (requestIntervalMillis > defaultMaxRequestIntervalMillis) {
    		
    		logger.warn("Invalid heartbeat request interval {}, greater than {} millis", requestIntervalMillis, defaultMaxRequestIntervalMillis);    		
    		
    		requestIntervalMillis = defaultMaxRequestIntervalMillis;
    	}
    	
    	logger.info("Heartbeat request interval: {} millis", requestIntervalMillis);
    	
    	this.requestIntervalMillis = requestIntervalMillis;
	}
	
	@EventListener
	public void onApplicationEvent(ApplicationReadyEvent event) {
		executor.scheduleWithFixedDelay(() -> execute(), 0, requestIntervalMillis, TimeUnit.MILLISECONDS);
	}

	private void execute() {
		if (heartbeatManager.execute()) {
			
			dispose();
		}
	}
	
	@PreDestroy
	public void dispose() {
		if (executor != null && !executor.isShutdown()) {
			executor.shutdown();

			logger.info("Heartbeat monitor terminated");
		}
	}
	
}
