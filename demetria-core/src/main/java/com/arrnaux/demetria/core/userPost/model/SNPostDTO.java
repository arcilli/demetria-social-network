    package com.arrnaux.demetria.core.userPost.model;

    import com.arrnaux.demetria.core.userAccount.model.SNUser;
    import lombok.*;

    @Setter
    @Getter
    @AllArgsConstructor()
    @NoArgsConstructor()
    @Builder

    /**
     * Used a member instead of extending SNPost because of downcast problems.
     */
    public class SNPostDTO {
        private SNPost post;
        private SNUser owner;

    }