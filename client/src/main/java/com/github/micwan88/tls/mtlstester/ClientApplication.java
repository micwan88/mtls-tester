package com.github.micwan88.tls.mtlstester;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

	Logger logger = LoggerFactory.getLogger(ClientApplication.class);

	private HttpWebClient httpWebClient;

	public ClientApplication(HttpWebClient httpWebClient) {
		this.httpWebClient = httpWebClient;
	}

	public static void main(String[] args) {
		//Show JVM ssl debug for this tester
		System.setProperty("javax.net.debug", "ssl:handshake:keymanager:trustmanager");
		//Or we can use -Dspring-boot.run.jvmArguments="-Djavax.net.debug=ssl:handshake:keymanager:trustmanager"
		
		SpringApplication.run(ClientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("#############################################################");
		System.out.println(this.httpWebClient.execute());
		logger.info("#############################################################");
	}
}
