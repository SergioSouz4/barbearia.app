package com.barbearia.barbearia_app.service;

import org.springframework.stereotype.Service;

@Service
public class CaptchaService {

    // Implementação simples - você pode integrar com reCAPTCHA ou outro serviço
    public boolean validarCaptcha(String captchaToken) {
        // Por enquanto, aceita qualquer token não vazio
        // Você pode implementar validação com Google reCAPTCHA aqui
        return captchaToken != null && !captchaToken.trim().isEmpty();
    }
}