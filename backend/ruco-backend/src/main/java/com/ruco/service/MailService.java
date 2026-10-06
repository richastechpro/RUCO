package com.ruco.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.postmarkapp.postmark.Postmark;
import com.postmarkapp.postmark.client.ApiClient;
import com.postmarkapp.postmark.client.data.model.message.Message;
import com.postmarkapp.postmark.client.data.model.message.MessageResponse;
import com.postmarkapp.postmark.client.exception.PostmarkException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

	@Value("${postmark.host.address}")
	private String from;

	@Value("${postmark.api.token}")
	private String apiToken;

	private static volatile ApiClient client = null;

	private ApiClient getClient() {
		if (client == null) {
			synchronized (MailService.class) {
				if (client == null) {
					client = Postmark.getApiClient(apiToken);
					client.setDebugMode();
					log.info("Postmark API client initialized...");
				}
			}
		}
		return client;
	}

	public void sendPostMarkEmail(String to, String subject, String body) throws IOException, PostmarkException {
		Message message = new Message();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setHtmlBody(body);
		message.setMessageStream("outbound");
		log.info("To: {}", to);

		MessageResponse response = getClient().deliverMessage(message);
		log.info("Mail status, Response: {}", response.getMessage());

	}

}