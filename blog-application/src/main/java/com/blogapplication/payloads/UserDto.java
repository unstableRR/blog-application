package com.blogapplication.payloads;

import com.blogapplication.entities.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDto { //important so that main data is not exposed.

    private int id;

    @NotEmpty
    @Size(min = 3, message = "username must be min of 3 characters")
    private String name;

    @Email(message = "enter valid email")
    private String email;

    //@JsonIgnore
    @NotEmpty
    @Size(min = 3, max = 10, message="password must be min of 3 chars and max of 10 chars")
    private String password;

    @NotNull
    private String about;

    private Set<RoleDto> roles = new HashSet<>();

}
