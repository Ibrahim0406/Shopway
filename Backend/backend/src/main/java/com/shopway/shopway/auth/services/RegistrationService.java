package com.shopway.shopway.auth.services;

import com.shopway.shopway.auth.dto.RegistrationRequest;
import com.shopway.shopway.auth.dto.RegistrationResponse;
import com.shopway.shopway.auth.entities.User;
import com.shopway.shopway.auth.helper.VerificationCodeGenerator;
import com.shopway.shopway.auth.repositories.UserDetailRepository;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;


/*
 * Servis za registraciju i verifikaciju korisnika.
 */
@Builder
@Service
public class RegistrationService {

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserDetailRepository userDetailRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    /*
     * Kreira novog korisnika u sistemu.
     * Proverava da li email već postoji, generiše verifikacioni kod,
     * hešira lozinku i šalje email sa verifikacionim kodom.
     *
     * @param request objekat sa podacima za registraciju (ime, prezime, email, lozinka, telefon)
     * @return RegistrationResponse sa statusom:
     *         - 200 i "User registered successfully" ako je uspešno
     *         - 400 i "Email already exists" ako email već postoji
     * @throws ServerErrorException ako dođe do greške prilikom čuvanja korisnika
     */
    public RegistrationResponse createUser(RegistrationRequest request) {

        User existing = userDetailRepository.findByEmail(request.getEmail());


        if (null != existing) {
            return RegistrationResponse.builder().code(400).message("Email already exists").build();

        }
        try{

            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setEnabled(false);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setProvider("manual");

            String code = VerificationCodeGenerator.generateCode();
            user.setVerificationCode(code);
            user.setAuthorities(authorityService.getUserAuthority());
            userDetailRepository.save(user);

            emailService.sendMail(user);

            return RegistrationResponse.builder()
                    .code(200)
                    .message("User registered successfully")
                    .build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ServerErrorException(e.getMessage(), e.getCause());
        }

    }

    /*
     * Verifikuje korisnika postavljanjem enabled flaga na true.
     * Poziva se nakon što korisnik unese ispravan verifikacioni kod.
     *
     * @param userName email korisnika koji se verifikuje
     */
    public void verifyUser(String userName) {
        User user = userDetailRepository.findByEmail(userName);
        user.setEnabled(true);
        userDetailRepository.save(user);
    }
}
