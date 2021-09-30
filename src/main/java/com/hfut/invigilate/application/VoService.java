package com.hfut.invigilate.application;

import com.hfut.invigilate.domain.user.vlaueobject.dto.UserDTO;

import java.util.List;

public interface VoService {
    List<UserDTO> getUser();
}
