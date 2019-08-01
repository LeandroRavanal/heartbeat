package io.github.lr.heartbeat.modes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HeartbeatSlaveMode implements HeartbeatMode {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String SLAVE = "SLAVE";
	
	@Override
	public String process(Runnable task) {
		logger.debug("Heartbeat mode is Slave");
		
		return SLAVE;
	}
	
}
