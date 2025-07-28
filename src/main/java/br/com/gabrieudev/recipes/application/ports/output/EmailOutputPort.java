package br.com.gabrieudev.recipes.application.ports.output;

public interface EmailOutputPort {
    boolean sendEmail(String to, String subject, String body);
}
