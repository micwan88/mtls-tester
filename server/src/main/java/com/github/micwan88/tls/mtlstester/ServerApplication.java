package com.github.micwan88.tls.mtlstester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

	public static void main(String[] args) {
		//Show JVM ssl debug for this tester
		System.setProperty("javax.net.debug", "ssl:handshake:keymanager:trustmanager");
		//Or we can use -Dspring-boot.run.jvmArguments="-Djavax.net.debug=ssl:handshake:keymanager:trustmanager"

		SpringApplication.run(ServerApplication.class, args);
	}
}
