package org.proha.utils;
import jakarta.enterprise.context.ApplicationScoped;
import org.proha.model.dto.UserDTO;
import org.proha.model.entity.User;

@ApplicationScoped
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getUsername(), "");
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setUsername(userDTO.getUsername());
        return user;
    }
}
