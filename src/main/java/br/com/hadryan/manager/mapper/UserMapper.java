package br.com.hadryan.manager.mapper;

import br.com.hadryan.manager.mapper.request.UserPostRequest;
import br.com.hadryan.manager.mapper.response.UserResponse;
import br.com.hadryan.manager.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User postToUser(UserPostRequest request);

    UserResponse userToResponse(User user);

}
