package io.github.lr.heartbeat.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.github.lr.heartbeat.processors.AppProcessor;

@Controller
@RequestMapping("/api/v1")
public class ModeController {

	public final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private AppProcessor appProcessor;
	
	private Runnable task = new Runnable() {
		@Override
		public void run() {
			logger.info("Task executed");
		}
	};
	
	@RequestMapping(value = "/mode", method = RequestMethod.GET)
	public ResponseEntity<String> mode() throws InterruptedException {
		
		return new ResponseEntity<>(appProcessor.process(task), HttpStatus.OK);
	}
	
	
}
