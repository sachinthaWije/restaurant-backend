package sachi.dev.restaurant.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private SendGrid sendGrid;

    public void sendRegistrationEmail(String to, String name) throws IOException {
        Email from = new Email("infoswphotographylk@gmail.com");
        String subject = "Registration Confirmation";
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", "Dear " + name + ",\n\nThank you for registering with our service!");
        Mail mail = new Mail(from, subject, toEmail, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);

        if (response.getStatusCode() != 202) {
            throw new IOException("Failed to send email. Status code: " + response.getStatusCode());
        }
    }
}
