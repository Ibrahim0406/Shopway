package com.shopway.shopway.controllers;

import com.shopway.shopway.dto.AddressRequest;
import com.shopway.shopway.entities.Address;
import com.shopway.shopway.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

/*
 * REST kontroler za upravljanje adresama korisnika.
 */
@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;
    /*
     * Kreira novu adresu za trenutno prijavljenog korisnika.
     * Principal automatski identifikuje korisnika preko JWT tokena.
     *
     * @param addressRequest DTO sa podacima adrese (ulica, grad, država, poštanski broj, telefon)
     * @param principal objekat sa informacijama o autentifikovanom korisniku
     * @return ResponseEntity sa kreiranom Address i HTTP statusom 200 OK
     */
    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody AddressRequest addressRequest, Principal principal) {
        Address address = addressService.createAddress(addressRequest,principal);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }
    /*
     * Briše adresu po ID-u.
     * Ne proverava da li adresa pripada trenutno prijavljenom korisniku
     * (potencijalni sigurnosni problem - trebalo bi dodati proveru).
     *
     * @param id UUID adrese koja se briše
     * @return ResponseEntity sa HTTP statusom 200 OK
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable UUID id) {
       addressService.deleteAddress(id);
       return new ResponseEntity<>(HttpStatus.OK);
    }
}
