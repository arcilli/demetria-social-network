package com.arrnaux.userservice.userAccount.data;

public interface SequenceDAO {

    long getNextSequenceId(String key);

}