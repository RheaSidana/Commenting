package org.vijayi.commenting.comment.service;

import org.junit.jupiter.api.Test;
import org.vijayi.commenting.comment.exceptions.EmptyMessageException;
import org.vijayi.commenting.comment.exceptions.UnableToAddCommentInDbException;
import org.vijayi.commenting.comment.repository.CommentRepository;
import org.vijayi.commenting.comment.repository.model.Comment;
import org.vijayi.commenting.comment.view.model.request.AddCommentRequestBody;
import org.vijayi.commenting.user.exceptions.InvalidUserNameException;
import org.vijayi.commenting.user.exceptions.UnableToAddUserToDbException;
import org.vijayi.commenting.user.exceptions.UserNotInDbException;
import org.vijayi.commenting.user.repository.model.User;
import org.vijayi.commenting.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {
    @Test
    public void shouldNotAddCommentWhenInvalidUserName() {
        User mockUser = mock(User.class);
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        UserService mockUserService = mock(UserService.class);
        when(mockAddCommentRequestBody.getPostedBy()).thenReturn(mockUser);
        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedBy())).thenReturn(false);

        verify(mockAddCommentRequestBody).getPostedBy();
        assertEquals(mockUser, mockAddCommentRequestBody.getPostedBy());

        InvalidUserNameException invalidUserNameException = assertThrows(
                InvalidUserNameException.class,
                () -> new CommentService(mockUserService).addComment(mockAddCommentRequestBody)
        );
        assertEquals("PostedBy: invalid name provided", invalidUserNameException.getMessage());

        verify(mockUserService).isValidUserName(mockAddCommentRequestBody.getPostedBy());
        assertFalse(mockUserService.isValidUserName(mockUser));
    }

    @Test
    public void shouldNotAddCommentWhenUserNotInDb() throws InvalidUserNameException, UserNotInDbException {
        User mockUser = mock(User.class);
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        UserService mockUserService = mock(UserService.class);
        when(mockAddCommentRequestBody.getPostedBy()).thenReturn(mockUser);
        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedBy())).thenReturn(false);

        verify(mockAddCommentRequestBody, times(2)).getPostedBy();
        assertEquals(mockUser, mockAddCommentRequestBody.getPostedBy());

        UserNotInDbException userNotInDbException = assertThrows(
                UserNotInDbException.class,
                () -> new CommentService(mockUserService).addComment(mockAddCommentRequestBody)
        );
        assertEquals("PostedBy: user not found", userNotInDbException.getMessage());

        verify(mockUserService).isValidUserName(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isValidUserName(mockUser));

        verify(mockUserService).isAvailableInDb(mockAddCommentRequestBody.getPostedBy());
        assertFalse(mockUserService.isAvailableInDb(mockUser));
    }

    @Test
    public void shouldNotAddCommentWhenUnableToAddInDb() throws InvalidUserNameException, UserNotInDbException {
        User mockUser = mock(User.class);
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        UserService mockUserService = mock(UserService.class);
        when(mockAddCommentRequestBody.getPostedBy()).thenReturn(mockUser);
        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedFor())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedFor())).thenReturn(false);
        when(mockUserService.addUserToDb(any())).thenReturn(null);

        verify(mockAddCommentRequestBody, times(2)).getPostedBy();
        assertEquals(mockUser, mockAddCommentRequestBody.getPostedBy());

        UnableToAddUserToDbException unableToAddUserToDbException = assertThrows(
                UnableToAddUserToDbException.class,
                () -> new CommentService(mockUserService).addComment(mockAddCommentRequestBody)
        );
        assertEquals("PostedFor: unable to add user to db", unableToAddUserToDbException.getMessage());

        verify(mockUserService).isValidUserName(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isValidUserName(mockUser));

        verify(mockUserService).isAvailableInDb(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isAvailableInDb(mockUser));
    }

    @Test
    public void shouldNotAddCommentWhenUserAlreadyInDbAndUnableToAddCommentInDb() throws InvalidUserNameException, UserNotInDbException {
        User mockUser = mock(User.class);
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        UserService mockUserService = mock(UserService.class);
        CommentRepository mockCommentRepository = mock(CommentRepository.class);

        when(mockAddCommentRequestBody.getPostedBy()).thenReturn(mockUser);
        when(mockAddCommentRequestBody.getPostedFor()).thenReturn(mockUser);
        when(mockAddCommentRequestBody.getMessage()).thenReturn("Dummy Message.");

        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedFor())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedFor())).thenReturn(true);
        when(mockCommentRepository.save(any())).thenReturn(null);

        verify(mockAddCommentRequestBody, times(2)).getPostedBy();
        assertEquals(mockUser, mockAddCommentRequestBody.getPostedBy());

        UnableToAddCommentInDbException unableToAddCommentInDb = assertThrows(
                UnableToAddCommentInDbException.class,
                () -> new CommentService(
                        mockCommentRepository, mockUserService
                ).addComment(mockAddCommentRequestBody)
        );
        assertEquals("failed to add comment in db", unableToAddCommentInDb.getMessage());

        verify(mockUserService, times(2)).isValidUserName(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isValidUserName(mockUser));

        verify(mockUserService, times(2)).isAvailableInDb(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isAvailableInDb(mockUser));
    }

    @Test
    public void shouldNotAddCommentWhenMessageIsEmpty() throws InvalidUserNameException, UserNotInDbException, UnableToAddUserToDbException, UnableToAddCommentInDbException, EmptyMessageException {
        User mockUser = mock(User.class);
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        UserService mockUserService = mock(UserService.class);
        CommentRepository mockCommentRepository = mock(CommentRepository.class);
        Comment mockComment = mock(Comment.class);

        when(mockAddCommentRequestBody.getPostedBy()).thenReturn(mockUser);
        when(mockAddCommentRequestBody.getPostedFor()).thenReturn(mockUser);
        when(mockAddCommentRequestBody.getMessage()).thenReturn("");

        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedFor())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedFor())).thenReturn(true);
        when(mockCommentRepository.save(any())).thenReturn(mockComment);

        verify(mockAddCommentRequestBody, times(2)).getPostedBy();
        assertEquals(mockUser, mockAddCommentRequestBody.getPostedBy());

        EmptyMessageException emptyMessageException = assertThrows(
                EmptyMessageException.class,
                () -> new CommentService(
                        mockCommentRepository, mockUserService
                ).addComment(mockAddCommentRequestBody)
        );
        assertEquals("Message is empty or null.", emptyMessageException.getMessage());

        verify(mockUserService, times(2)).isValidUserName(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isValidUserName(mockUser));

        verify(mockUserService, times(2)).isAvailableInDb(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isAvailableInDb(mockUser));
    }

    @Test
    public void shouldNotAddCommentWhenMessageIsNull() throws InvalidUserNameException, UserNotInDbException, UnableToAddUserToDbException, UnableToAddCommentInDbException, EmptyMessageException {
        User mockUser = mock(User.class);
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        UserService mockUserService = mock(UserService.class);
        CommentRepository mockCommentRepository = mock(CommentRepository.class);
        Comment mockComment = mock(Comment.class);

        when(mockAddCommentRequestBody.getPostedBy()).thenReturn(mockUser);
        when(mockAddCommentRequestBody.getPostedFor()).thenReturn(mockUser);
        when(mockAddCommentRequestBody.getMessage()).thenReturn(null);

        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedFor())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedFor())).thenReturn(true);
        when(mockCommentRepository.save(any())).thenReturn(mockComment);

        verify(mockAddCommentRequestBody, times(2)).getPostedBy();
        assertEquals(mockUser, mockAddCommentRequestBody.getPostedBy());

        EmptyMessageException emptyMessageException = assertThrows(
                EmptyMessageException.class,
                () -> new CommentService(
                        mockCommentRepository, mockUserService
                ).addComment(mockAddCommentRequestBody)
        );
        assertEquals("Message is empty or null.", emptyMessageException.getMessage());

        verify(mockUserService, times(2)).isValidUserName(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isValidUserName(mockUser));

        verify(mockUserService, times(2)).isAvailableInDb(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isAvailableInDb(mockUser));
    }

    @Test
    public void shouldAddComment() throws InvalidUserNameException, UserNotInDbException, UnableToAddUserToDbException, UnableToAddCommentInDbException, EmptyMessageException {
        User mockUser = mock(User.class);
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        UserService mockUserService = mock(UserService.class);
        CommentRepository mockCommentRepository = mock(CommentRepository.class);
        Comment mockComment = mock(Comment.class);

        when(mockAddCommentRequestBody.getPostedBy()).thenReturn(mockUser);
        when(mockAddCommentRequestBody.getPostedFor()).thenReturn(mockUser);
        when(mockAddCommentRequestBody.getMessage()).thenReturn("Dummy Message.");

        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedFor())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedFor())).thenReturn(true);
        when(mockCommentRepository.save(any())).thenReturn(mockComment);

        verify(mockAddCommentRequestBody, times(2)).getPostedBy();
        assertEquals(mockUser, mockAddCommentRequestBody.getPostedBy());

        CommentService commentService = new CommentService(mockCommentRepository, mockUserService);
        commentService.addComment(mockAddCommentRequestBody);

        verify(mockUserService, times(2)).isValidUserName(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isValidUserName(mockUser));

        verify(mockUserService, times(2)).isAvailableInDb(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isAvailableInDb(mockUser));
    }


}