package ru.prog.itmo.connection;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

public class TokenValidator {
    private static final String secretKey = "SJ#(а12&е$DF";

    public boolean validate(String token, String userLogin){
        try {
            var verifier = buildVerifier(userLogin);
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            return false;
        }
    }

    private JWTVerifier buildVerifier(String login){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.require(algorithm)
                .withIssuer("UltraMarines")
                .withClaim("login", login)
                .build();
    }

}
