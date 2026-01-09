package com.shopway.shopway.auth.services;


import com.shopway.shopway.auth.entities.User;
import com.shopway.shopway.auth.repositories.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/*
 * Servis za rad sa OAuth2 autentifikacijom (Google Sign-In).
 */
@Service
public class OAuth2Service {

    @Autowired
    UserDetailRepository userDetailRepository;

    @Autowired
    private AuthorityService authorityService;

    /*
     * Pronalazi korisnika u bazi po email adresi.
     *
     * @param userName email korisnika
     * @return User objekat ako postoji, null ako ne postoji
     */
    public User getUser(String userName) {
        return userDetailRepository.findByEmail(userName);
    }

    /*
     * Kreira novog korisnika na osnovu OAuth2 podataka.
     * Korisnik se automatski aktivira (enabled = true) jer je već verifikovan preko Google-a.
     * Ne postavlja se lozinka jer korisnik koristi OAuth2 za prijavu.
     *
     * @param oAuth2User OAuth2User objekat sa podacima iz Google-a
     * @param provider naziv providera (npr. "google")
     * @return kreirani i sačuvani User objekat
     */
    public User createUser(OAuth2User oAuth2User, String provider) {
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String email = oAuth2User.getAttribute("email");

        User user = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .provider(provider)
                .enabled(true)
                .authorities(authorityService.getUserAuthority()).build();
        return userDetailRepository.save(user);
    }
}
