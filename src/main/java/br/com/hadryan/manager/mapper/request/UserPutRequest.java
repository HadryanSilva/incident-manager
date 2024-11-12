package br.com.hadryan.manager.mapper.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPutRequest {

    private Long id;

    private String username;

    private String password;

    private String email;

}
