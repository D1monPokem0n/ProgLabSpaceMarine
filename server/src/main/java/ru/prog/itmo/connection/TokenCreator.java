package ru.prog.itmo.connection;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TokenCreator {
    private final Algorithm algorithm;
    private static long REFRESH_TOKEN_LIFE_TIME = 60;
    private static long ACCESS_TOKEN_LIFE_TIME = 30;
    private static final String secretKey = "SJ#(а12&е$DF";

    public TokenCreator(){
        algorithm = Algorithm.HMAC256(secretKey);
    }

    public String createAccessToken(String login) {
        try {
            return JWT.create()
                    .withIssuer("UltraMarines")
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(ACCESS_TOKEN_LIFE_TIME, ChronoUnit.SECONDS))
                    .withClaim("login", login)
                    .sign(algorithm);
        } catch (JWTCreationException e){
            throw new InvalidTokenException("Не удалось создать токен");
        }
    }
   /*

    */


    public String createRefreshToken(String login){
        try {
            return JWT.create()
                    .withIssuer("UltraMarines")
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plus(REFRESH_TOKEN_LIFE_TIME, ChronoUnit.SECONDS))
                    .withClaim("login", login)
                    .sign(algorithm);
        } catch (JWTCreationException e){
            throw new InvalidTokenException("Не удалось создать токен");
        }
    }

    public static void setAccessTokenLifeTime(long lifeTime){
        ACCESS_TOKEN_LIFE_TIME = lifeTime;
    }

    public static long getAccessTokenLifeTime() {
        return ACCESS_TOKEN_LIFE_TIME;
    }

    public static void setRefreshTokenLifeTime(long lifeTime){
        REFRESH_TOKEN_LIFE_TIME = lifeTime;
    }

    public static long getRefreshTokenLifeTime() {
        return REFRESH_TOKEN_LIFE_TIME;
    }
}
