package com.shopway.shopway.auth.exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;

public class RESTAuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {

    /*
     * Metoda koja se poziva kada neautorizovani korisnik pokuša da pristupi zaštićenim resursima.
     * Šalje HTTP 401 Unauthorized odgovor klijentu.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"401 Unauthorized");
    }
}
