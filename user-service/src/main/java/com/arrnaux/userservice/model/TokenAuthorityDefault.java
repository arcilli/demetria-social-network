package com.arrnaux.userservice.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@Log4j
public class TokenAuthorityDefault implements TokenAuthority {
    private final String KEY_PHRASE = "KcT4V4PeMwKmwAqv";
    private final String ISSUER = "DEMETRIA";
    private Algorithm usedAlgorithm = Algorithm.HMAC256(KEY_PHRASE);

    @Override
    // TODO: rename this
    public Token generateToken(SNUser user) {
        try {
            log.info("Generating token for user: " + user);
            return buildToken(user);
        } catch (JWTCreationException e) {
            e.printStackTrace();
            log.error("Error at JWTCreationException");
        }
        return null;
    }

    private Token buildToken(SNUser snUser) {
        // TODO: add here USER characteristics
        // TODO: add an expiration date
        // TODO: add a session ID (maybe encrypt id)
        String rawToken = JWT.create()
                .withIssuer(ISSUER)
                .withSubject("userInfo")
//                .withExpiresAt(Date.UTC()+ datetime.timedelta(seconds=30))
                .withClaim("firstName", snUser.getFirstName())
                .withClaim("lastName", snUser.getLastName())
                .withClaim("email", snUser.getEmail())
                .sign(usedAlgorithm);
        return new Token(rawToken);
    }

    // TODO: add a method for signature check (tokenValitation)
    // A token is considered valid when all the restrictions specified in its header & payload are satisfied.
    @Override
    public boolean tokenIsValid(Token token) {
        try {
            JWTVerifier verifier = JWT.require(usedAlgorithm)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT jwt = verifier.verify(token.getValue());
            // TODO: check the time stamps
            // TODO: check @jwt for any specific things
            // TODO: this should be encrypted?
            Map<String, Claim> claims = jwt.getClaims();
            return true;
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();
            log.error("Invalid signature/claims");
        }
        return false;
    }
}
