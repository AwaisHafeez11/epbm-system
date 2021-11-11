package com.app.epbmsystem.util;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SmsNotification {
    private final String ACCOUNT_SID ="ACcdd9a1a5492010ba88facc8c6be96986";  //twilio account SID
    private final String AUTH_TOKEN = "d524b0021dd3240d1bc4c1b1c5fc7c8b";   //twilio account TOKEN
    private final String FROM_NUMBER = "+15075775234";                     //twilio TRIAL Number


    public ResponseEntity<Object> Notification(String toNumber, String userMessage){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(new PhoneNumber(toNumber), new PhoneNumber(FROM_NUMBER), userMessage)
                .create();
        System.out.println("here is my id:"+message.getSid());// Unique resource ID created to manage this transaction
        return new ResponseEntity<>("The message has been successfully sent to: "+toNumber, HttpStatus.OK);
    }

}
