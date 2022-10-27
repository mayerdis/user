package org.mick.user.configuration;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.mick.user.Entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.Serializable;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

@Component
public class JsonWebTokenUtils implements Serializable {

    @Value("${jwt.secret}")
    public String secret;
    @Value("${jwt.expiration}")
    public Integer expiration;

    private Key getKey(){
        return new SecretKeySpec(DatatypeConverter.parseBase64Binary(secret)
                , SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(User u) {
        Date dateExp = Date.from(LocalDateTime.now().plusMinutes(expiration).atZone(ZoneId.systemDefault()).toInstant());
        String[] authorities = new String[1];
        authorities[0] = "ROLE_ADMIN";
        return Jwts.builder().setClaims(new HashMap<>())
                .setSubject(u.getEmail())
                .setIssuer(u.getName())
                .claim("authorities", authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(dateExp)
                .signWith(getKey()).compact();
    }
}
