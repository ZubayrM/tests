package com.zubayr.tests.config;

import com.zubayr.tests.model.User;
import com.zubayr.tests.repository.UserRepository;
import com.zubayr.tests.security.UserDetailImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenAdapter {

    private final UserRepository userRepository;
    private String secret = "zubayr";


    @Autowired
    public JwtTokenAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String createToken(String username){
        Date date = new Date();
        Claims claims = Jwts.claims();
        claims.setSubject(username);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() * 100_000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Authentication authentication(String token){
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        if (!claims.getExpiration().before(new Date())){
            User user = userRepository.findByUserName(claims.getSubject()).orElseThrow();
            UserDetailImpl detail = UserDetailImpl.getInstance(user);
            System.out.println("список ролей" + detail.getAuthorities());
            return new UsernamePasswordAuthenticationToken(detail, null, detail.getAuthorities());
        } else {
            throw new UsernameNotFoundException("sorry");
        }

    }
}
