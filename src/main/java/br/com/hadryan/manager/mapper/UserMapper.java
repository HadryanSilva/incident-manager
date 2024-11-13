package br.com.hadryan.manager.mapper;

import br.com.hadryan.manager.mapper.request.UserPostRequest;
import br.com.hadryan.manager.mapper.response.UserResponse;
import br.com.hadryan.manager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User postToUser(UserPostRequest request);

    UserResponse userToResponse(User user);

}
