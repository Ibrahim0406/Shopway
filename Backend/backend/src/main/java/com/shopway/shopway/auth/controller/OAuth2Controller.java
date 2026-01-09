package com.shopway.shopway.auth.controller;

import com.shopway.shopway.auth.config.JWTTokenHelper;
import com.shopway.shopway.auth.entities.User;
import com.shopway.shopway.auth.services.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/*
 * REST kontroler za OAuth2 autentifikaciju (Google Sign-In).
 */
@RestController
@CrossOrigin
@RequestMapping("/oauth2")
public class OAuth2Controller {

    @Autowired
    OAuth2Service oAuth2Service;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    /*
     * Callback endpoint koji se poziva nakon uspešne OAuth2 autentifikacije sa Google-om.
     * Proverava da li korisnik već postoji u bazi, ako ne - kreira ga.
     * Generiše JWT token i redirektuje korisnika na frontend sa tokenom.
     *
     * @param oAuth2User OAuth2 korisnik sa podacima iz Google-a (email, ime, prezime)
     * @param response HTTP response za redirekciju
     * @throws IOException ako redirekcija ne uspe
     */
    @GetMapping("/success")
    public void callbackOAuth2(@AuthenticationPrincipal OAuth2User oAuth2User, HttpServletResponse response) throws IOException {

        String userName = oAuth2User.getAttribute("email");

        User user= oAuth2Service.getUser(userName);

        if (null == user){
            user = oAuth2Service.createUser(oAuth2User, "google");
        }

        String token = jwtTokenHelper.generateToken(user.getUsername());

        response.sendRedirect("http://localhost:5173/oauth2/callback?token=" + token);
    }


}
