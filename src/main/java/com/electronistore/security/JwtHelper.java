package com.electronistore.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Step to implement JWT:
 *         1. add the required depenedency like security, Jwt (api, impl) and jjwt-jackson
 *         2. Create JWTHelper class. This class contains method related to perform operations with jwt token like
 *            generateToken, validateToken etc.
 *
 *
 * THis is used to perform jwt operations
 * jwt generate
 * username nikalne k liye bhi is class ka use karenge
 */


@Component
public class JwtHelper {

    /**
     * Requirement ->
     * 1. Validity means how much user will be logged in.
     * 2. To create Secret Key
     */

    // We took 30 minutes
    public static final long TOKEN_VALIDITY = 30 * 60 * 1000;


    public static final String SECRET_KEY = "asdfnjdkshuwiesvjbwstiouqeffie4w8349jefsbviq29roidfviu3h938rubjfkjsdbh";

//    @Value("${asdfnjdkshuwiesvjbwstiouqeffie4w8349jefsbviq29roidfviu3h938rubjfkjsdbh}")
//    private   String SECRET_KEY;
    // Retrive username and jwt token
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token){
//        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();


        /**
         * 0.12.5 version
         *
         * remove all deprecation before deployment
         *
         * */
        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseSignedClaims(token).getPayload();


//        return Jwts.parser().setSigningKey(SECRET_KEY).build().parseClaimsJwt(token).getPayload();

/**
        return  Jwts.parser()
                .verifyWith(getPublicSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();*/
    }


    // Check if the token has expired ?
    public Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    // retrieve expiration date from jwt token using Claims interface
    public Date getExpirationDateFromToken(String token){
//        return getClaimFromToken(token, Claims::getExpiration);
        return getClaimFromToken(token, claims -> claims.getExpiration());
    }


    // generate token for user
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject){
       /* return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.ES512, SECRET_KEY).compact();*/
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //the token will be expired in 10 hours
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
}


