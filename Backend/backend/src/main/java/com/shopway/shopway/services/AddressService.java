package com.shopway.shopway.services;

import com.shopway.shopway.auth.entities.User;
import com.shopway.shopway.dto.AddressRequest;
import com.shopway.shopway.entities.Address;
import com.shopway.shopway.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.UUID;
/*
 * Servis za upravljanje adresama korisnika.
 */
@Service
public class AddressService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AddressRepository addressRepository;

    /*
     * Kreira novu adresu za trenutno prijavljenog korisnika.
     * Automatski povezuje adresu sa korisnikom preko Principal objekta.
     *
     * @param addressRequest DTO sa podacima adrese (ulica, grad, država, poštanski broj, telefon)
     * @param principal objekat sa informacijama o autentifikovanom korisniku
     * @return sačuvana Address sa generisanim ID-em
     */
    public Address createAddress(AddressRequest addressRequest, Principal principal) {
        User user = (User) userDetailsService.loadUserByUsername(principal.getName());
        Address address = Address.builder()
                .street(addressRequest.getStreet())
                .city(addressRequest.getCity())
                .state(addressRequest.getState())
                .zipCode(addressRequest.getZipCode())
                .phoneNumber(addressRequest.getPhoneNumber())
                .user(user)
                .build();
        return addressRepository.save(address);
    }

    /*
     * Briše adresu po ID-u.
     *
     * NAPOMENA: Ova metoda ne proverava da li adresa pripada trenutno prijavljenom korisniku.
     * To predstavlja potencijalni sigurnosni problem jer bilo koji autentifikovani korisnik
     * može obrisati tuđu adresu ako zna njen ID.
     *
     * @param id UUID adrese koja se briše
     */
    public void deleteAddress(UUID id) {
        addressRepository.deleteById(id);
    }
}
