package com.arrnaux.userservice.userAccount.model;

public interface TokenAuthority {

    Token generateToken(SNUser user);

    boolean tokenIsValid (Token token);
}
