package io.github.otavioborgsm.service.impl;

import io.github.otavioborgsm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.FileNotFoundException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String email;

    @Override
    public void enviarEmail(String para, String titulo, String texto) throws MessagingException{
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(email);
        simpleMailMessage.setTo(para);
        simpleMailMessage.setSubject(titulo);
        simpleMailMessage.setText(texto);
        emailSender.send(simpleMailMessage);
    }

    @Override
    public void emailComAnexo(String para, String titulo, String texto, String anexo) throws MessagingException, FileNotFoundException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setTo(para);
        messageHelper.setSubject(titulo);
        messageHelper.setText(texto);
        FileSystemResource file = new FileSystemResource(ResourceUtils.getFile(anexo));
        messageHelper.addAttachment("Purchase Order", file);
        emailSender.send(mimeMessage);
    }
}
