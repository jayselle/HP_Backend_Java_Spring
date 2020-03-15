package com.jayselle.copynet.components;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component(value = "tokendecoder")
public class TokenDecoder {

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";
    private final String SECRET = "mySecretKey";

    public Long getIdUser(HttpServletRequest request){
        Claims claims = getClaims(request);
        return Long.parseLong(claims.getId());
    }

    private Claims getClaims(HttpServletRequest request) {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX,"");
        return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

}
