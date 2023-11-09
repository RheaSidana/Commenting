package org.vijayi.commenting.user.service;

import org.junit.jupiter.api.Test;
import org.vijayi.commenting.user.repository.UserRepository;
import org.vijayi.commenting.user.repository.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void shouldIsAvailableInDbWhenNotInDb() {
        UserRepository mockUserRepo = mock(UserRepository.class);
        User mockUser = mock(User.class);
        String username = "TempoName";
        when(mockUser.getName()).thenReturn(username);
        when(mockUser.getId()).thenReturn(Long.parseLong("1"));
        when(mockUserRepo.findByName(mockUser.getName(), mockUser.getId())).thenReturn(Optional.empty());
        UserService userService = new UserService(mockUserRepo);

        boolean availableInDb = userService.isAvailableInDb(mockUser);

        assertFalse(availableInDb);
        verify(mockUser, times(2)).getName();
        assertEquals(username, mockUser.getName());
        verify(mockUserRepo).findByName(mockUser.getName(), mockUser.getId());
        assertTrue(mockUserRepo.findByName(mockUser.getName(), mockUser.getId()).isEmpty());
    }

    @Test
    public void shouldIsAvailableInDb() {
        UserRepository mockUserRepo = mock(UserRepository.class);
        User mockUser = mock(User.class);
        String username = "TempoName";
        when(mockUser.getName()).thenReturn(username);
        when(mockUser.getId()).thenReturn(Long.parseLong("1"));
        when(mockUserRepo.findByName(mockUser.getName(), mockUser.getId())).thenReturn(Optional.of(mockUser));
        UserService userService = new UserService(mockUserRepo);

        boolean availableInDb = userService.isAvailableInDb(mockUser);

        assertTrue(availableInDb);
        verify(mockUserRepo).findByName(mockUser.getName(), mockUser.getId());
        assertEquals(Optional.of(mockUser), mockUserRepo.findByName(mockUser.getName(), mockUser.getId()));
    }

    @Test
    public void shouldNotAddUserToDb() {
        UserRepository mockUserRepo = mock(UserRepository.class);
        User mockUser = mock(User.class);
        String username = "TempoName";
        when(mockUser.getName()).thenReturn(username);
        when(mockUserRepo.save(any())).thenReturn(null);
        UserService userService = new UserService(mockUserRepo);

        User user = userService.addUserToDb(mockUser);

        assertNull(user);
    }

    @Test
    public void shouldAddUserToDb() {
        UserRepository mockUserRepo = mock(UserRepository.class);
        User mockUser = mock(User.class);
        String username = "TempoName";
        when(mockUser.getName()).thenReturn(username);
        when(mockUserRepo.save(any())).thenReturn(mockUser);
        UserService userService = new UserService(mockUserRepo);

        User user = userService.addUserToDb(mockUser);

//        assertNotNull(user);
        assertEquals(mockUser, user);
    }
}