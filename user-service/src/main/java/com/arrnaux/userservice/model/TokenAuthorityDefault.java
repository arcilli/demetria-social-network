package com.arrnaux.userservice.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TokenAuthorityDefault implements TokenAuthority {
    private final static Logger logger = Logger.getLogger(TokenAuthorityDefault.class);
    private final String KEY_PHRASE = "SECRET";
    private final String ISSUER = "DEMETRIA";
    private Algorithm usedAlgorithm = Algorithm.HMAC256(KEY_PHRASE);

    @Override
    // TODO: rename this
    public Token getToken(SNUser user) {
        try {
            logger.info("Generating token for user: " + user);
            return createTokenForUser(user);
        } catch (JWTCreationException e) {
            e.printStackTrace();
            logger.error("Error at JWTCreationException");
        }
        return null;
    }

    private Token createTokenForUser(SNUser snUser) {
        // TODO: add here USER characteristics
        String rawToken = JWT.create()
                .withIssuer(ISSUER)
                .withClaim("firstName", snUser.getFirstName())
                .withClaim("lastName", snUser.getLastName())
                .withClaim("email", snUser.getEmail())
                .sign(usedAlgorithm);
        return new Token(rawToken);
    }

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
            logger.error("Invalid signature/claims");
        }
        return false;
    }
}
