package com.sharan.room;

import lombok.extern.log4j.Log4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:security.xml")
@Log4j
public class RoomManagementLauncher {
	
	public static void main(String[] args) {
		log.info("RoomManagementLauncher started...");
		SpringApplication.run(RoomManagementLauncher.class, args);
	}
}
