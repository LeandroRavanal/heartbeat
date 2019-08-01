package io.github.lr.heartbeat.modes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HeartbeatMasterMode implements HeartbeatMode {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String MASTER = "MASTER";
	
	@Override
	public String process(Runnable task) {
		logger.debug("Heartbeat mode is Master");

		task.run();
		
		return MASTER;
	}
	
}
