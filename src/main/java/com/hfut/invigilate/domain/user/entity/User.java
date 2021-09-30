package com.hfut.invigilate.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.sound.midi.Soundbank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String name;

    private String workId;

    private String college;

    private String telephone;

    @JsonIgnore
    private String password;

    private List<UserRole> roles;

}