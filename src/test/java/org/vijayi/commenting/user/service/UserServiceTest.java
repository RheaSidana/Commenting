package org.vijayi.commenting.user.service;

import org.junit.jupiter.api.Test;
import org.vijayi.commenting.user.repository.model.User;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {
    @Test
    public void shouldNotIsValidUserNameWhenSpecialChar() {
        User user = new User();
        user.setName("MyDummyName@");
        UserService userService = new UserService();

        boolean validUserName = userService.isValidUserName(user);

        assertFalse(validUserName);
    }

    @Test
    public void shouldNotIsValidUserNameWhenSymbol() {
        User user = new User();
        user.setName("MyDummy#Name");
        UserService userService = new UserService();

        boolean validUserName = userService.isValidUserName(user);

        assertFalse(validUserName);
    }

    @Test
    public void shouldNotIsValidUserNameWhenHasNumber() {
        User user = new User();
        user.setName("MyDummyName1");
        UserService userService = new UserService();

        boolean validUserName = userService.isValidUserName(user);

        assertFalse(validUserName);
    }

    @Test
    public void shouldNotIsValidUserNameWhenEmpty() {
        User user = new User();
        user.setName("");
        UserService userService = new UserService();

        boolean validUserName = userService.isValidUserName(user);

        assertFalse(validUserName);
    }

    @Test
    public void shouldIsValidUserName() {
        User user = new User();
        user.setName("MyDummyName");
        UserService userService = new UserService();

        boolean validUserName = userService.isValidUserName(user);

        assertTrue(validUserName);
    }
}