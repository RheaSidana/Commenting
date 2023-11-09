package org.vijayi.commenting.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vijayi.commenting.user.repository.UserRepository;
import org.vijayi.commenting.user.repository.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService() {
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static final String USER_NAME_REGEX = "^[a-zA-Z]+$";

    public boolean isValidUserName(User user) {
        return user.getName().matches(USER_NAME_REGEX);
    }

    public boolean isAvailableInDb(User user) {
        if(user.getId() == 0){
            return false;
        }
        return userRepository.findByName(user.getName(), user.getId()).isPresent();
    }

    public User addUserToDb(User user){
        User userToAddInDb = new User();
        userToAddInDb.setName(user.getName());

        User userCreated = userRepository.save(userToAddInDb);
        return userCreated;
    }
}
