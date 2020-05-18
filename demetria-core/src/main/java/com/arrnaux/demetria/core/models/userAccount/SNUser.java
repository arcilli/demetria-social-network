package com.arrnaux.demetria.core.models.userAccount;

import com.mongodb.lang.Nullable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@AllArgsConstructor
@Builder

@Document(collection = "user")
public class SNUser extends SNUserLoginDTO {

    @Id
    protected String id;

    @NotNull
    protected String firstName;

    @NotNull
    protected String lastName;

    @NotNull
    protected String userName;

    @Nullable
    protected String profileImageBase64;

    /**
     * Adapter from SNUserRegistrationDTO to SNUser
     *
     * @param snUserRegistrationDTO represents the extended information for an user (the user with validation for
     *                              password)
     */
    public SNUser(SNUserRegistrationDTO snUserRegistrationDTO) {
        super(snUserRegistrationDTO.getEmail(), snUserRegistrationDTO.getPassword());
        this.firstName = snUserRegistrationDTO.getFirstName();
        this.lastName = snUserRegistrationDTO.getLastName();
        this.userName = snUserRegistrationDTO.getUserName();
    }

    public void updateObjectWithNotNullValues(SNUser modifiedUser) {
        if (null != modifiedUser.getLastName()) this.setLastName(modifiedUser.getLastName());
        if (null != modifiedUser.getFirstName()) this.setFirstName(modifiedUser.getFirstName());
    }

    public SNUser obfuscateUserInformation() {
        this.setPassword("");
        return this;
    }

    /**
     * Used in Thymleaf.
     *
     * @return a full name of an user.
     */
    public String getFullName() {
        StringBuilder stringBuilder = new StringBuilder();
        if (null != firstName) {
            stringBuilder.append(firstName);
            if (null != lastName) {
                stringBuilder.append(' ').append(lastName);
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SNUser snUser = (SNUser) o;
        return Objects.equals(id, snUser.id) &&
                Objects.equals(userName, snUser.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName);
    }
}