package com.arrnaux.demetria.core.userPost.model;


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

@Document(collection = "post")

public class SNPost {
    @Id
    protected String id;

    protected long ownerId;

    protected String content;

    protected PostVisibility visibility;

    protected List<Vote> votes;
}