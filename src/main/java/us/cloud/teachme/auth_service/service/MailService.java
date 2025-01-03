package us.cloud.teachme.auth_service.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import us.cloud.teachme.auth_service.model.ActivationCode;

@Service
public class MailService {

  @Value("${sendgrid.api-key}")
  private String SEND_GRID_API_KEY;

  @Value("${sendgrid.from}")
  private String SENDGRID_FROM;

  @Value("${base-url}")
  private String BASE_URL;

  public void sendActivationMail(ActivationCode code) {
    Email from = new Email(SENDGRID_FROM);
    String subject = "[Teachme] Activate your account";
    Email to = new Email(code.getEmail());
    Content content = new Content("text/html", "Your activation code is: " + code.getId() + "<br>Click <a href='"
        + BASE_URL + "/api/v1/auth/activate?code=" + code.getId() + "'>here</a> to activate your account");
    Mail mail = new Mail(from, subject, to, content);

    SendGrid sg = new SendGrid(SEND_GRID_API_KEY);
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      sg.api(request);
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }

}
