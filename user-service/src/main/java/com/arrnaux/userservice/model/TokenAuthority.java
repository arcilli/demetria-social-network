package com.arrnaux.userservice.model;

public interface TokenAuthority {

    public Token getToken(SNUser user);

    public boolean tokenIsValid (Token token);
}
