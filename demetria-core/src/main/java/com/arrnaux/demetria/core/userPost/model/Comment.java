package com.arrnaux.demetria.core.userPost.model;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private String id;

    private SNUser owner;

    private String content;

    private Date creationDate;
}
