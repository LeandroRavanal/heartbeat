package io.github.lr.heartbeat.modes;

public interface HeartbeatMode {

	public String process(Runnable task);
	
}
