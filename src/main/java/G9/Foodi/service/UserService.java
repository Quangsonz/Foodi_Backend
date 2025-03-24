package G9.Foodi.service;

import java.util.List;

import G9.Foodi.dto.UserDto;
import G9.Foodi.model.User;

public interface UserService {
    List<User> getAllUsers();
    User createUser(UserDto userDto);
    void deleteUser(String id);
    boolean isAdmin(String email);
    User makeAdmin(String id);
}