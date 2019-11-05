package com.arrnaux.userservice.userPost.model;

import lombok.*;
import lombok.extern.log4j.Log4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Log4j
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Document

public class SNPost {
    @Id
    protected long id;

    protected long ownerId;

    protected String content;

    protected PostVisibility visibility;

    protected List<Vote> votes;
}
