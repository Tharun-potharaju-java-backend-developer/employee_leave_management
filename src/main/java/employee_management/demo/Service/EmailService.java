package employee_management.demo.Service;

import employee_management.demo.DTO.Attachment;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

// use this service for sending emails with data nad attachments
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(
            String to,
            String subject,
            String text,
            List<Attachment> attachments
    ) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        for (Attachment attachment : attachments) {

            helper.addAttachment(
                    attachment.fileName(),
                    new ByteArrayResource(attachment.data())
            );
        }

        mailSender.send(message);
    }
}