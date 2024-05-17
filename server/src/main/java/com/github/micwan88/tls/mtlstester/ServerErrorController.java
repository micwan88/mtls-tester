package com.github.micwan88.tls.mtlstester;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ServerErrorController implements ErrorController {
    @RequestMapping(value = "/error", method = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.DELETE,
        RequestMethod.PUT
    })
    @ResponseBody
    public ResponseEntity<String> errorPage(
            HttpServletRequest request,
            @Value("${mtlstester.server.contentType:json}") String contentType
        ) {
        final HttpHeaders headers = new HttpHeaders();
        String responseContent = "";

        if (contentType.equalsIgnoreCase("json")) {
            headers.setContentType(MediaType.APPLICATION_JSON);
            responseContent = "{\"error\": \"$errorcode\"}";
        } else if (contentType.equalsIgnoreCase("xml")) {
            headers.setContentType(MediaType.APPLICATION_XML);
            responseContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><error>$errorcode</error>";
        } else if (contentType.equalsIgnoreCase("soap")) {
            headers.setContentType(new MediaType("application", "soap+xml"));
            responseContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" soap:encodingStyle=\"http://www.w3.org/2003/05/soap-encoding\"><soap:Body><soap:Fault><faultcode>soap:Client</faultcode><faultstring>$errorcode</faultstring></soap:Fault></soap:Body></soap:Envelope>";
        } else {
            headers.setContentType(MediaType.TEXT_HTML);
            responseContent = "<html><body>$errorcode</body></html>";
        }

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            responseContent = responseContent.replaceAll("\\$errorcode", status.toString());
        } else {
            responseContent = responseContent.replaceAll("\\$errorcode", "Unknown Error");
        }
        return new ResponseEntity<>(responseContent, headers, HttpStatus.valueOf(Integer.valueOf(status.toString())));
    }
}
