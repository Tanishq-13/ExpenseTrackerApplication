package com.example.demo.model;

import com.example.demo.entities.UserInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

//model class to use in our code not in databases or anything
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserInfoDto extends UserInfo {

//    @Getter
//    @Setter

    private String userName;

    private String lastName;

    private String email;

    private Long phoneNumber;
}
