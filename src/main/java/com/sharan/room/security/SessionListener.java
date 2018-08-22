package com.sharan.room.security;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Log4j
@Configuration
public class SessionListener implements HttpSessionListener {

	@Value("${sessionTimeout}")
	String sessionTimeout;
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();

		session.setMaxInactiveInterval(Integer.parseInt(sessionTimeout));
		
		log.info("[Session Created] SESSION ID : [ '" + session.getId() + "' ];");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		log.info("[Session Destroyed] SESSION ID : [ '" + session.getId() + "' ];");
	}
}