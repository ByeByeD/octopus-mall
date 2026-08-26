package com.octopusmall.common.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire-ms}")
    private long expireMs;

    //缓存密钥对象，只构建一次
    private SecretKey signKey;

    /**
     * bean初始化完成后执行一次，只生成一次密钥
     */
    @PostConstruct
    public void initKey() {
        this.signKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey getSignKey() {
        return this.signKey;
    }

    /**
     * 生成token，userId字符串
     */
    public String generateToken(String userId) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", userId);
        Date now = new Date();
        Date expire = new Date(now.getTime() + expireMs);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(getSignKey())
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public long getRemainExpireMs(Claims claims) {
        Date expiration = claims.getExpiration();
        long remain = expiration.getTime() - System.currentTimeMillis();
        return Math.max(remain, 0);
    }
}
