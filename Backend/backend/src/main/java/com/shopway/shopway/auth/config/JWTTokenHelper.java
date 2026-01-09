package com.shopway.shopway.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/*
 * Helper klasa za rad sa JWT tokenima - generisanje, validacija i parsiranje.
 */

@Component
public class JWTTokenHelper {

    @Value("${jwt.auth.app}")
    private String appName;

    @Value("${jwt.auth.secret_key}")
    private String secretKey;

    @Value("${jwt.auth.expires_in}")
    private int expiresIn;


    /*
    * Generiše JWT token za dati korisnički naziv (userName).
    */
    public String generateToken(String userName){
        return Jwts.builder()
                .issuer(appName)
                .subject(userName)
                .issuedAt(new Date())
                .expiration(generateExpirationDate())
                .signWith(getSigningKey())
                .compact();
    }
    /*
    * Kreira i vraća ključ za potpisivanje tokena koristeći tajni ključ iz konfiguracije.
    * */
    private Key getSigningKey() {
        byte[] keysBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keysBytes);
    }
    /*
     * Generiše datum isteka tokena na osnovu konfiguracije (trenutno vreme + expiresIn sekundi).
     * @return Date objekat koji predstavlja vreme isteka
     */
    private Date generateExpirationDate() {
        return new Date((new Date()).getTime() + expiresIn * 1000L);
    }

    /*
     * Izvlači JWT token iz Authorization headera HTTP zahteva.
     * @param request HTTP zahtev
     * @return token string bez "Bearer " prefiksa
     */

    public String getToken(HttpServletRequest request) {

        String authHeader = getAuthHeaderFromHeader(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return authHeader;
    }

    /*
     * Validira JWT token proveravajući da li username odgovara i da li token nije istekao.
     * @param token JWT token
     * @param userDetails detalji korisnika
     * @return true ako je token validan, false inače
     */


    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /*
     * Proverava da li je token istekao.
     * @param token JWT token
     * @return true ako je token istekao, false inače
     */

    private boolean isTokenExpired(String token) {
        Date expireDate = getExpirationDate(token);
        return expireDate.before(new Date());
    }

    /*
     * Izvlači datum isteka iz tokena.
     * @param token JWT token
     * @return Date objekat sa vremenom isteka
     */
    private Date getExpirationDate(String token) {
        Date expireDate;
        try{
            final Claims claims = this.getAllClaimsFromToken(token);
            expireDate = claims.getExpiration();
        }catch (Exception e){
            expireDate = null;
        }
        return expireDate;
    }


    /*
     * Izvlači Authorization header iz HTTP zahteva.
     * @param request HTTP zahtev
     * @return vrednost Authorization headera
     */

    private String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    /*
     * Izvlači username (subject) iz JWT tokena.
     * @param authToken JWT token
     * @return username/email korisnika
     */
    public String getUsernameFromToken(String authToken) {
        String username ;
        try{
            final Claims claims = this.getAllClaimsFromToken(authToken);
            username = claims.getSubject();
        }catch (Exception e){
            username = null;
        }
        return username;
    }

    /*
     * Parsira JWT token i izvlači sve claims (podatke) iz njega.
     * @param token JWT token
     * @return Claims objekat sa svim podacima iz tokena
     */
    private Claims getAllClaimsFromToken (String token){
        Claims claims;

        try{
            claims = Jwts.parser().setSigningKey(getSigningKey()).build()
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

}
