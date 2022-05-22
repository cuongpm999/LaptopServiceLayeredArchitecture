package vn.ptit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import vn.ptit.entities.mysql.Order;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;


@Service
public class SendMailService {
	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	TemplateEngine templateEngine;

	public void sendMail(Order order) throws MessagingException {
		Context ctx = new Context();
		ctx.setVariable("order", order);

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
		message.setSubject("THANH TOÁN THÀNH CÔNG " + order.getCreatedAt().toString());
		message.setFrom("computercuongpham999@gmail.com");
		message.setTo(order.getUser().getEmail());
//		message.addAttachment("logo.png", new ClassPathResource("static/img/logo.png"));

		String htmlContent = templateEngine.process("mail.html", ctx);
		message.setText(htmlContent, true); // true = isHtml

		javaMailSender.send(mimeMessage);
	}

}
