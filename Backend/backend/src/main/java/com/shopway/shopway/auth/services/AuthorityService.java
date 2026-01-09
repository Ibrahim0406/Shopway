package com.shopway.shopway.auth.services;

import com.shopway.shopway.auth.entities.Authority;
import com.shopway.shopway.auth.repositories.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
 * Servis za upravljanje korisničkim ulogama (authorities/roles).
 */
@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;


    /*
     * Vraća default korisničku ulogu ("USER") za nove korisnike.
     * Koristi se prilikom registracije da se svakom novom korisniku dodeli osnovna uloga.
     *
     * @return lista sa jednom Authority ("USER")
     */
    public List<Authority> getUserAuthority(){
        List<Authority> authorities = new ArrayList<>();
        Authority authority = authorityRepository.findByRoleCode("USER");
        authorities.add(authority);
        return authorities;
    }

    /*
     * Kreira novu ulogu u sistemu.
     *
     * @param role kod uloge (npr. "ADMIN", "USER", "MODERATOR")
     * @param description opis uloge
     * @return kreirani i sačuvani Authority objekat
     */
    public Authority createAuthority(String role, String description){
        Authority authority = Authority.builder().roleCode(role).roleDescription(description).build();
        return authorityRepository.save(authority);
    }
}
