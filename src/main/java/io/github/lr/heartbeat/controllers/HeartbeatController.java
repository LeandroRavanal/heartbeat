package io.github.lr.heartbeat.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/api/v1")
public class HeartbeatController {
	
	public final Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "/heartbeat", method = RequestMethod.HEAD)
	public ResponseEntity<String> heartbeat() {
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
