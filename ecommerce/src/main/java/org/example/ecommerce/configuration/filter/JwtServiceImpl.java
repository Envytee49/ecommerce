package org.example.ecommerce.configuration.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String extractUserName(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractALlClaims(token);
        return claimsResolvers.apply(claims);
    }

    public Key getSiginKey(){
        byte[] key = Decoders.BASE64.decode("jhasdjfhasdfhasdfjasdkfjasdfasdfjadsjfasdjfahsdfhasdjkfhasdjkhfasdjkf");
        return Keys.hmacShaKeyFor(key);
    }

    public Claims extractALlClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody();
    }

    public boolean isValidToken(String token, UserDetails userDetails){
        String userName = extractUserName(token);
        return(userName.equals(userDetails.getUsername()) && !isExpiredToken(token));
    }

    private boolean isExpiredToken(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}
