package com.command_service.security;

import com.command_service.dao.UserDAO;
import com.command_service.exception.CustomException;
import com.command_service.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

@Component
public class TokenManager implements Serializable {

    private final Logger LOG = LogManager.getLogger(TokenManager.class);

    @Autowired
    UserDAO userDAO;

//    public static final long TOKEN_VALIDITY = 10 * 60 * 60;
    public static final long TOKEN_VALIDITY = 5 * 60 * 60;

    private final String jwtSecret = "qwertyu34iopasdfghjklzxcvbnm123236";

    public String generateJwtToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("user", userDetails.getName() + " " + userDetails.getLname());
        claims.put("id", userDetails.getId());
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public Boolean validateJwtTokenAndUser(String token, User userDetails) throws CustomException {
        String username = getUsernameFromToken(token);
//        Claims claims = Jwts.parser()
//                .setSigningKey(jwtSecret)
//                .parseClaimsJws(token)
//                .getBody();
        boolean isTokenExpired = validateJwtToken(token);
        return (username.equals(userDetails.getEmail()) && !isTokenExpired);
    }

    public Boolean validateJwtToken(String token) throws CustomException {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            boolean isTokenExpired = claims.getExpiration().before(new Date());
            return !isTokenExpired;
        } catch (Exception e) {
            throw new CustomException("Token expired");
        }

    }

    public String getUsernameFromToken(String token) {
        final Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return  claims.getSubject();
    }


    public String getAuthentication(HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");
        if (token != null) {
            // parse the token.
            String user;
            try {
                user = Jwts.parser()
                        .setSigningKey(jwtSecret)
                        .parseClaimsJws(token.replace("Bearer ", ""))
                        .getBody()
                        .getSubject();
            } catch (Exception e) {
                if(e instanceof ExpiredJwtException) {
                    LOG.error("Token expired: " + e.getMessage());
//                    new ResponseEntity<>("Token expired : ", HttpStatus.UNAUTHORIZED);
                    throw new ExpiredJwtException(null, null, "Token expired");
                }
                LOG.error(e.getMessage());
                throw new Exception("Token expired: " + e.getMessage());
            }
            return user;
        }
        return null;
    }

    public List<GrantedAuthority> getAuthorities(List<String> permissions) {
        List<GrantedAuthority> authList = new ArrayList<>();
        for (String permission: permissions) {
            authList.add(new SimpleGrantedAuthority(permission));
        }
        return authList;
    }

}
