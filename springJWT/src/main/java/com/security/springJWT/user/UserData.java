package com.security.springJWT.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="UserInfo", uniqueConstraints = @UniqueConstraint(columnNames = "user_email"))
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_email")
    private String userEmail;
    private String fullName;
    private String password;
    private String gender;
}
