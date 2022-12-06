package io.github.otavioborgsm.service;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;

public interface EmailService {
    void enviarEmail(String para, String titulo, String texto) throws MessagingException;
    void emailComAnexo(String para, String titulo, String texto, String anexo) throws MessagingException, FileNotFoundException;
}
