package com.deviget.component;

import com.deviget.config.ApplicationProperties;
import com.deviget.types.TokenDataDTO;
import com.google.gson.Gson;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.GregorianCalendar;

@Component
public class JwtManager {
    private ApplicationProperties applicationProperties;

    @Autowired
    public void setApplicationProperties(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String generateToken(String username, String password) {
        Gson gson = new Gson();
        String jwtPayLoad = gson.toJson(generateJwtPayload(username,20201188L));
        System.out.println("GeneratePayload => " + jwtPayLoad);
        // TODO what exactly
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.MINUTE, Integer.parseInt(applicationProperties.getTimeExpirationToken()));

        return Jwts.builder()
                .setSubject(jwtPayLoad)
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS256, applicationProperties.getTokenSecret().getBytes())
                .compact();
    }

    private String parseToken(String token) {
        String resp = "";
        try {
             resp = Jwts.parser().setSigningKey(applicationProperties.getTokenSecret().getBytes()).parseClaimsJws(token).getBody().getSubject();
        }catch (Exception e){

        }

        return resp;
    }

    public String generateJwtPayload(String username, Long code) {
        return username + ":" + code;
    }


    public boolean validateToken(String token) throws JwtException {
       Gson gson = new Gson();
        parseToken(gson.toJson(token));
        return true;
    }

    public TokenDataDTO parseTokenToModel(String token) {

        token = token.replace("Bearer ", "");

        String parseToken = parseToken(token.trim());

        String[] split = parseToken.split(":");

        if (split.length <= 0)
            return null;

        String username = split[0];

        return new TokenDataDTO(username);
    }
}
