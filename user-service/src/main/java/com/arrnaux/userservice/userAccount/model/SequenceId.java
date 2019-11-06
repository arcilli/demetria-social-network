package com.arrnaux.userservice.userAccount.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
//db.sequence.insert({_id: "hosting",seq: 0})
@Document(collection = "sequence")
public class SequenceId {
    @Id
    private String id;

    private long seq;
}
