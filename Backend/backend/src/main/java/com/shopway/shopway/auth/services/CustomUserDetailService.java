package com.shopway.shopway.auth.services;

import com.shopway.shopway.auth.entities.User;
import com.shopway.shopway.auth.repositories.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


/*
 * Implementacija UserDetailsService interfejsa za učitavanje korisničkih podataka.
 * Spring Security koristi ovaj servis za autentifikaciju korisnika.
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserDetailRepository userDetailRepository;

    /*
     * Učitava korisnika iz baze podataka po email adresi (username).
     * Ova metoda se automatski poziva od strane Spring Security-ja
     * prilikom autentifikacije (login) ili validacije JWT tokena.
     *
     * @param username email adresa korisnika
     * @return UserDetails objekat (User implementira UserDetails interfejs)
     * @throws UsernameNotFoundException ako korisnik sa datim emailom ne postoji
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDetailRepository.findByEmail(username);
        if (null == user){
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
