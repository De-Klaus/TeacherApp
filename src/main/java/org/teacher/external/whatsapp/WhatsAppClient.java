package org.teacher.external.whatsapp;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Component;

import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;

@Component
public class WhatsAppClient {

    private final String fromNumber;

    public WhatsAppClient(@Value("${twilio.account.sid}") String accountSid,
                          @Value("${twilio.auth.token}") String authToken,
                          @Value("${twilio.whatsapp.from}") String fromNumber) {
        Twilio.init(accountSid, authToken);
        this.fromNumber = fromNumber;
    }

    public void sendMessage(String toNumber, String message) {
        Message.creator(
                new PhoneNumber("whatsapp:" + toNumber),
                new PhoneNumber("whatsapp:" + fromNumber),
                message
        ).create();
    }
}
