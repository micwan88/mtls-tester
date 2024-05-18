package com.github.micwan88.tls.mtlstester;

import java.security.Principal;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ServerController {
    Logger logger = LoggerFactory.getLogger(ServerController.class);
    
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/user*", method = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.DELETE,
        RequestMethod.PUT
    }, consumes = { "*/*" })
    public ResponseEntity<String> user(
            Principal principal,
            @RequestHeader HttpHeaders requestHeaders,
            @RequestBody(required = false) String requestPayload,
            @Value("${mtlstester.server.contentType:json}") String contentType,
            @Value("${mtlstester.server.responseContent:{\"hello\"}}") String responseContent
        ) {

        logger.debug("Got user request ...");

        requestHeaders.forEach((headerKey, headerValues) -> {
            logger.debug("RequestHeader: {} -> {}", headerKey, 
                headerValues.stream().collect(Collectors.joining(",")));
        });

        logger.debug("RequestPayload: {}", requestPayload);

        logger.debug("Construct response ...");

        UserDetails currentUser = (UserDetails) ((Authentication) principal).getPrincipal();

        logger.info("currentUser: {}", currentUser);

        final HttpHeaders headers = new HttpHeaders();

        if (contentType.equalsIgnoreCase("json")) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        } else if (contentType.equalsIgnoreCase("xml")) {
            headers.setContentType(MediaType.APPLICATION_XML);
        } else if (contentType.equalsIgnoreCase("soap")) {
            headers.setContentType(new MediaType("application", "soap+xml"));
        } else {
            headers.setContentType(MediaType.TEXT_HTML);
        }

        String transformedContent = responseContent.replaceAll("\\$username", currentUser.getUsername());
        
        return new ResponseEntity<>(transformedContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value = { 
        "/",
        "/index*"
    }, method = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.DELETE,
        RequestMethod.PUT
    }, consumes = { "*/*" })
    public ResponseEntity<String> home(
            @RequestHeader HttpHeaders requestHeaders,
            @RequestBody(required = false) String requestPayload,
            @Value("${mtlstester.server.contentType:json}") String contentType,
            @Value("${mtlstester.server.responseContent:{\"hello\"}}") String responseContent
        ) {

        logger.debug("Got home request ...");

        requestHeaders.forEach((headerKey, headerValues) -> {
            logger.debug("RequestHeader: {} -> {}", headerKey, 
                headerValues.stream().collect(Collectors.joining(",")));
        });

        logger.debug("RequestPayload: {}", requestPayload);

        logger.debug("Construct response ...");

        final HttpHeaders headers = new HttpHeaders();

        if (contentType.equalsIgnoreCase("json")) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        } else if (contentType.equalsIgnoreCase("xml")) {
            headers.setContentType(MediaType.APPLICATION_XML);
        } else if (contentType.equalsIgnoreCase("soap")) {
            headers.setContentType(new MediaType("application", "soap+xml"));
        } else {
            headers.setContentType(MediaType.TEXT_HTML);
        }
        
        return new ResponseEntity<>(responseContent.replaceAll("\\$username", "Hello!"), headers, HttpStatus.OK);
    }
}
