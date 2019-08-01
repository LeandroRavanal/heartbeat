package io.github.lr.heartbeat.processors;

import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Component;

import io.github.lr.heartbeat.modes.HeartbeatMode;

@Component
public class AppProcessor {

	private HeartbeatMode mode;
	private CountDownLatch countDownLatch = new CountDownLatch(1);
	
	public synchronized void setMode(HeartbeatMode mode) {
		this.mode = mode;
		
		this.countDownLatch.countDown();
	}
	
	private void check() throws InterruptedException {
		if (mode == null) {
			countDownLatch.await();
		}
	}
	
	public String process(Runnable task) throws InterruptedException {
		check();
		
		return mode.process(task);
	}
	
}
