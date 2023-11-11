package org.vijayi.commenting.comment.service;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.vijayi.commenting.comment.exceptions.EmptyMessageException;
import org.vijayi.commenting.comment.exceptions.InvalidRequestException;
import org.vijayi.commenting.comment.exceptions.UnableToAddCommentInDbException;
import org.vijayi.commenting.comment.repository.CommentRepository;
import org.vijayi.commenting.comment.repository.model.Comment;
import org.vijayi.commenting.comment.view.model.request.AddCommentRequestBody;
import org.vijayi.commenting.user.exceptions.InvalidUserNameException;
import org.vijayi.commenting.user.exceptions.UnableToAddUserToDbException;
import org.vijayi.commenting.user.exceptions.UserNotInDbException;
import org.vijayi.commenting.user.repository.model.User;
import org.vijayi.commenting.user.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    public void shouldNotShowCommentsWhenInvalidRequest() throws UserNotInDbException, InvalidRequestException {
        User user = new User(1L, "dummy");
        CommentService commentService = new CommentService();

        InvalidRequestException invalidRequestException = assertThrows(
                InvalidRequestException.class,
                () -> commentService.showComments(1, 2L, user)
        );

        String exceptionMessage = "User does not have access to comments.";
        assertEquals(exceptionMessage, invalidRequestException.getMessage());
    }

    @Test
    public void shouldNotShowCommentsWhenInvalidUser() throws UserNotInDbException, InvalidRequestException {
        User user = new User(1L, "dummy");
        UserService mockUserService = mock(UserService.class);
        when(mockUserService.isValidUserId(any())).thenReturn(new User());
        CommentService commentService = new CommentService(mockUserService);

        UserNotInDbException userNotInDbException = assertThrows(
                UserNotInDbException.class,
                () -> commentService.showComments(1, 1L, user)
        );

        String exceptionMessage = "User is not present in the db.";
        assertEquals(exceptionMessage, userNotInDbException.getMessage());
    }

    @Test
    public void shouldShowCommentsReturnEmptyListWhenNoCommentIsPresent() throws UserNotInDbException, InvalidRequestException {
        User user = new User(1L, "dummy");
        UserService mockUserService = mock(UserService.class);
        when(mockUserService.isValidUserId(any())).thenReturn(user);
        CommentRepository mockCommentRepository = mock(CommentRepository.class);
        List<Comment> comments = new ArrayList<>();
        Page<Comment> page = new PageImpl<>(comments);
        when(mockCommentRepository.findAllByPostedFor(any(), any())).thenReturn(page);
        CommentService commentService = new CommentService(mockCommentRepository, mockUserService);

        List<Comment> commentsPostedFor = commentService.showComments(1, 1L, user);

        assertEquals(new ArrayList<Comment>(), commentsPostedFor);
    }

    @Test
    public void shouldNotShowCommentsWhenInvalidPageNumber() throws UserNotInDbException, InvalidRequestException {
        User user = new User(1L, "dummy");
        UserService mockUserService = mock(UserService.class);
        when(mockUserService.isValidUserId(1L)).thenReturn(user);
        CommentService commentService = new CommentService(mockUserService);

        InvalidRequestException invalidRequestException = assertThrows(
                InvalidRequestException.class,
                () -> commentService.showComments(-1, 1L, user)
        );

        String exceptionMessage = "Invalid Page number provided";
        assertEquals(exceptionMessage, invalidRequestException.getMessage());
    }

    @Test
    public void shouldShowComments() throws UserNotInDbException, InvalidRequestException {
        User user = new User(1L, "dummy");
        UserService mockUserService = mock(UserService.class);
        when(mockUserService.isValidUserId(any())).thenReturn(user);
        CommentRepository mockCommentRepository = mock(CommentRepository.class);
        List<Comment> expectedComments = Arrays.asList(
                new Comment(1L, "Test Comment 1", Timestamp.from(Instant.now()), user.getId(), user.getId()),
                new Comment(2L, "Test Comment 2", Timestamp.from(Instant.now()), user.getId(), user.getId())
        );
        Page<Comment> page = new PageImpl<>(expectedComments);
        when(mockCommentRepository.findAllByPostedFor(any(), any())).thenReturn(page);
        CommentService commentService = new CommentService(mockCommentRepository, mockUserService);

        List<Comment> commentsPostedFor = commentService.showComments(1, 1L, user);

        assertNotNull(commentsPostedFor);
        assertEquals(expectedComments.size(), commentsPostedFor.size());
        assertEquals(expectedComments, commentsPostedFor);
    }
}