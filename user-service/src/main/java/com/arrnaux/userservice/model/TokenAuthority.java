package com.arrnaux.userservice.model;

public interface TokenAuthority {

    Token generateToken(SNUser user);

    boolean tokenIsValid (Token token);
}
