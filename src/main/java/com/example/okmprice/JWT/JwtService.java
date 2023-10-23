package com.example.okmprice.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private String secretKey = "2l6a2Wb2PkZJaztUT44lNUNytjIXZzaGDuMM0He0CKubOpg8zSSoFuuhf4DaKzo3nai4ZkBAk4TR30du2aQFdecCXstf0AwnNOJVmYJc3ok6pJPT3KrHTNxShGjBksAC2NTN8e4EeSWpXL6hi3csSEYmLDvuvSh5BCIm3m7uUVgAty8X3f2w71buxtTgvMmE4aVaafxppWoRiJEbzI0pIiG8u1NQ8L3zOwsoRgvS1CA2GGmCIVSHrDinnnOo0CPR";
    private long jwtExpiration = 60 * 15 * 1000;
    private long refrestExpiration = 60 * 24 * 7;

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String generateToken(UserInfo userInfo){
        return generateToken(new HashMap<>(), userInfo);
    }

    public String generateToken(Map<String ,Object> extractClaims, UserInfo userInfo){
        return buildToken(extractClaims, userInfo, jwtExpiration);
    }

    public String buildToken(Map<String ,Object> extractClaims, UserInfo userInfo, long jwtExpiration){
        return Jwts.builder().setClaims(extractClaims).setSubject(userInfo.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
    }

    public boolean isTokenValid(String token, UserInfo userInfo){
        final String username = extractUsername(token);
        return (username.equals(userInfo.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }
    private Key getSignInKey(){
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

}
