package com.shopway.shopway.auth.controller;


import com.shopway.shopway.auth.dto.UserDetailsDto;
import com.shopway.shopway.auth.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


/*
 * REST kontroler za dobavljanje informacija o trenutno prijavljenom korisniku.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserDetailController {

    @Autowired
    private UserDetailsService userDetailsService;
    /*
     * Endpoint koji vraća profil trenutno prijavljenog korisnika.
     * Koristi Principal (automatski injektovan od strane Spring Security-ja)
     * da identifikuje prijavljenog korisnika.
     *
     * @param principal objekat koji sadrži informacije o autentifikovanom korisniku
     * @return UserDetailsDto sa podacima korisnika (ime, prezime, email, adrese, uloge)
     *         ili 401 Unauthorized ako korisnik nije pronađen
     */
    @GetMapping("/profile")
    public ResponseEntity<UserDetailsDto> getUserProfile(Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());

        if (null == user){
            return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
        }

        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhonenumber())
                .id(user.getId())
                .addressList(user.getAddressesList())
                .authorityList(user.getAuthorities().toArray())
                .build();
        return new ResponseEntity<>(userDetailsDto, HttpStatus.OK);
    }
}
