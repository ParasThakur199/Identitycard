package com.idcard.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.idcard.Model.UserEntity;
import com.idcard.Repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	  @Autowired
	    private UserRepository userRepository;
	    @Value("${jwt.secret-key}")
	    private String SECRET_KEY;

	    @Value("${jwt.token-expiration}")
	    private long TOKEN_EXPIRATION;
	    
	    private final long REFRESH_TOKEN_VALIDITY = 30 * 60 * 1000;

	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }

	    public String generateToken(UserDetails userDetails) {
	        return generateToken(new HashMap<>(), userDetails);
	    }

	    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
	        UserEntity user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
	        Long id = user.getId();

	        return Jwts
	                .builder()
	                .setClaims(extraClaims)
	                .setSubject(userDetails.getUsername())
	                .setId(String.valueOf(id))
	                .setIssuedAt(new Date(System.currentTimeMillis()))
	                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
	                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
	                .compact();
	    }
	    
	    
	    public String generatRefreshToken(String username) {
	    	return Jwts.builder().setSubject(username)
	    			.setIssuedAt(new Date(System.currentTimeMillis()))
	    			.setExpiration(new Date(System.currentTimeMillis()+ REFRESH_TOKEN_VALIDITY))
	    			.signWith(SignatureAlgorithm.HS256,SECRET_KEY)
	    			.compact();
	    }

	    public boolean isTokenValid(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }

	    private boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }

	    private Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    private Claims extractAllClaims(String token) {
	        return Jwts
	                .parserBuilder()
	                .setSigningKey(getSignInKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	    }

	    private Key getSignInKey() {
	        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
	        return Keys.hmacShaKeyFor(keyBytes);
	    }

	    public UserEntity getUserDetailsFromToken(String authorizationHeader) {
	        String token=authorizationHeader.substring(7);
	        Claims claims = Jwts.parser()
	                .setSigningKey(SECRET_KEY)
	                .parseClaimsJws(token)
	                .getBody();
	        return userRepository.findById(Long.valueOf(claims.getId())).orElseThrow(()->new UsernameNotFoundException("User not found"));
	    }
}
