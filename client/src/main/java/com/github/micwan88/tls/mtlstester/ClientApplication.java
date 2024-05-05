package com.github.micwan88.tls.mtlstester;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {
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
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println(this.httpWebClient.get());
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
	}
}
