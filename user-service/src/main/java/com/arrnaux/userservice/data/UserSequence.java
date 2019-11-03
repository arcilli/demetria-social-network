package com.arrnaux.userservice.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sNUser_sequence")
@Getter
@Setter
public class UserSequence {

    @Id
    private String id;

    private long seq;
}
