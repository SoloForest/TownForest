package com.ll.townforest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing // @EntityListeners(AuditingEntityListener.class) 가 작동하도록 허용
@EnableScheduling // @Scheduled 가 작동하도록 허용
public class TownforestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TownforestApplication.class, args);
	}

}
