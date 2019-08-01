package io.github.lr.heartbeat.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.lr.heartbeat.modes.HeartbeatMasterMode;
import io.github.lr.heartbeat.modes.HeartbeatSlaveMode;
import io.github.lr.heartbeat.processors.AppProcessor;
import io.github.lr.heartbeat.services.HeartbeatService;

@Component
public class HeartbeatManager {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String MASTER = "master";
	private static final String SLAVE = "slave";
	
	@Autowired private HeartbeatService heartbeatService; 
	
	@Autowired private AppProcessor appProcessor;
	@Autowired private HeartbeatMasterMode masterMode;
	@Autowired private HeartbeatSlaveMode slaveMode;
	
	private final String initialMode;
	private final int maxFailCount;
	
	private boolean initialized; 
	private int count;
	
	private final Object lock = new Object();
	
	public HeartbeatManager(@Value("${heartbeat.mode}") String mode, @Value("${heartbeat.maxFailCount}") int maxFailCount) {
		String initialMode = SLAVE;

		if (MASTER.equals(mode) || SLAVE.equals(mode)) {
		
			initialMode = MASTER.equals(mode) ? MASTER : SLAVE;
		} else {
			
			logger.warn("Invalid heartbeat mode value {}. Allowed values are master or slave.", mode); 
		}
		
		logger.info("Heartbeat initial mode: {}", initialMode);

		final int defaultMinFailCount = 1;
		final int defaultMaxFailCount = 10;
		
		if (maxFailCount < defaultMinFailCount || maxFailCount > defaultMaxFailCount) {
			
			logger.warn("Invalid heartbeat max fail count {}. Allowed values are beetwen {} to {}.", maxFailCount, defaultMinFailCount, defaultMaxFailCount);

			maxFailCount = maxFailCount < defaultMinFailCount ? defaultMinFailCount : defaultMaxFailCount;
		}
		
		logger.info("Heartbeat heartbeat max fail count: {}", maxFailCount);

		this.initialMode = initialMode;
		this.maxFailCount = maxFailCount;
		
		this.initialized = false; 
		this.count = 0;
	}
	
	public boolean isInitialModeMaster() {
		return MASTER.equals(initialMode);
	}
	
	public boolean execute() {
		return execute(heartbeatService.heartbeat());
	}
	
	private boolean execute(boolean active) {
		boolean shutdown = false; 
		
		synchronized (lock) {
			if (active) {
				if (!initialized) {
					initialized = true;
					
					appProcessor.setMode(slaveMode);
					
					logger.info("Heartbeat initialized");
					logger.info("Heartbeat mode: {}", SLAVE);
				}
				
				count = 0;
			} else {
				
				count++;
			}
			
			if (count >= maxFailCount) {
				count = 0;
				
				appProcessor.setMode(masterMode);

				if (!initialized) {
					initialized = true;
					
					logger.info("Heartbeat initialized");
				}
				logger.info("Heartbeat mode: {}", MASTER);
				
				shutdown = true;
			}
		}
		
		return shutdown;
	}

}
