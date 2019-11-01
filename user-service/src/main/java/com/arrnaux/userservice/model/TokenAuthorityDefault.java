package com.arrnaux.userservice.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class TokenAuthorityDefault implements TokenAuthority {
    private final static Logger logger = Logger.getLogger(TokenAuthorityDefault.class);
    private final String KEY_PHRASE = "SECRET";
    private final String ISSUER = "DEMETRIA";
    private Algorithm usedAlgorithm = Algorithm.HMAC256(KEY_PHRASE);

    @Override
    public Token getToken(SNUser user) {
        try {
            logger.info("Generating token for user: " + user);
            // TODO: add here USER characteristics
            String token = JWT.create()
                    .withIssuer(ISSUER)
                    .sign(usedAlgorithm);
            return new Token(token);
        } catch (JWTCreationException e) {
            e.printStackTrace();
            logger.error("Error at JWTCreationException");
        }
        return null;
    }

    @Override
    public boolean tokenIsValid(Token token) {
        try {
            JWTVerifier verifier = JWT.require(usedAlgorithm)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT jwt = verifier.verify(token.getValue());
            // check @jwt for any specific things
            return true;
        } catch (JWTVerificationException exception) {
            exception.printStackTrace();
            logger.error("Invalid signature/claims");
        }
        return false;
    }
}
