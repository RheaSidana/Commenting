package org.vijayi.commenting.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vijayi.commenting.user.repository.UserRepository;
import org.vijayi.commenting.user.repository.model.User;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public static final String USER_NAME_REGEX = "^[a-zA-Z]+$";

    public boolean isValidUserName(User user) {
        return user.getName().matches(USER_NAME_REGEX);
    }

    public boolean isAvailableInDb(User user){
        Optional<User> userInDb = userRepository.findByName(user.getName());

        if (userInDb.isEmpty()) {
            return false;
        }

        return true;
    }
}
