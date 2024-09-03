package sachi.dev.restaurant.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sachi.dev.restaurant.service.EmailService;

import java.io.IOException;

@SpringBootTest
public class EmailIntegrationTest {

    @Autowired
    private EmailService emailService;

    @Test
    void testSendRealEmail() throws IOException {
        emailService.sendRegistrationEmail("sachinthraneesh@gmail.com","subject","html content");
    }
}
