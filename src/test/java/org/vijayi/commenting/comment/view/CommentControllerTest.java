package org.vijayi.commenting.comment.view;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.UserCredentialsDataSourceAdapter;
import org.vijayi.commenting.comment.exceptions.EmptyMessageException;
import org.vijayi.commenting.comment.exceptions.InvalidRequestException;
import org.vijayi.commenting.comment.exceptions.UnableToAddCommentInDbException;
import org.vijayi.commenting.comment.repository.model.Comment;
import org.vijayi.commenting.comment.service.CommentService;
import org.vijayi.commenting.comment.view.model.request.AddCommentRequestBody;
import org.vijayi.commenting.comment.view.model.response.ResponseBodyError;
import org.vijayi.commenting.comment.view.model.response.AddCommentResponseBodySuccess;
import org.vijayi.commenting.user.exceptions.InvalidUserNameException;
import org.vijayi.commenting.user.exceptions.UnableToAddUserToDbException;
import org.vijayi.commenting.user.exceptions.UserNotInDbException;
import org.vijayi.commenting.user.repository.model.User;
import org.vijayi.commenting.user.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentControllerTest {
    @Test
    public void shouldNotAddCommentWhenInvalidUserNameException() throws InvalidUserNameException,
            UserNotInDbException, UnableToAddUserToDbException,
            UnableToAddCommentInDbException, EmptyMessageException {
        // arrange
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        CommentService mockCommentService = mock(CommentService.class);
        String exceptionMessage = "Exception";
        when(mockCommentService.addComment(mockAddCommentRequestBody)).
                thenThrow(new InvalidUserNameException(exceptionMessage));
        ResponseBodyError invalidRequest = new ResponseBodyError();
        invalidRequest.setMessage("Invalid Request");
        invalidRequest.setError(exceptionMessage);

        //act
        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<Object> stringResponseEntity = commentController.addComment(mockAddCommentRequestBody);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, stringResponseEntity.getStatusCode());
        assertEquals(invalidRequest, stringResponseEntity.getBody());

        verify(mockCommentService).addComment(mockAddCommentRequestBody);
        InvalidUserNameException invalidUserNameException = assertThrows(
                InvalidUserNameException.class, () -> mockCommentService.addComment(mockAddCommentRequestBody));
        assertEquals(exceptionMessage, invalidUserNameException.getMessage());
    }

    @Test
    public void shouldNotAddCommentWhenPostedByNotFoundInDb() throws InvalidUserNameException,
            UserNotInDbException, UnableToAddUserToDbException,
            UnableToAddCommentInDbException, EmptyMessageException {
        //arrange
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        CommentService mockCommentService = mock(CommentService.class);
        Comment mockComment = mock(Comment.class);
        String exceptionMessage = "Exception";
        when(mockCommentService.addComment(mockAddCommentRequestBody)).
                thenThrow(new UserNotInDbException(exceptionMessage));
        ResponseBodyError invalidRequest = new ResponseBodyError();
        invalidRequest.setMessage("Invalid Request");
        invalidRequest.setError(exceptionMessage);

        // act
        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<Object> stringResponseEntity = commentController.addComment(mockAddCommentRequestBody);

        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, stringResponseEntity.getStatusCode());
        assertEquals(invalidRequest, stringResponseEntity.getBody());

        verify(mockCommentService).addComment(mockAddCommentRequestBody);
        UserNotInDbException userNotInDbException = assertThrows(
                UserNotInDbException.class, () -> mockCommentService.addComment(mockAddCommentRequestBody));
        assertEquals(exceptionMessage, userNotInDbException.getMessage());
    }

    @Test
    public void shouldNotAddCommentWhenUnableToAddCommentInDb() throws InvalidUserNameException,
            UserNotInDbException, UnableToAddUserToDbException,
            UnableToAddCommentInDbException, EmptyMessageException {
        //arrange
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        CommentService mockCommentService = mock(CommentService.class);
        Comment mockComment = mock(Comment.class);
        String exceptionMessage = "Exception";
        when(mockCommentService.addComment(mockAddCommentRequestBody)).
                thenThrow(new UnableToAddCommentInDbException(exceptionMessage));
        ResponseBodyError invalidRequest = new ResponseBodyError();
        invalidRequest.setMessage("Invalid Request");
        invalidRequest.setError(exceptionMessage);

        // act
        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<Object> stringResponseEntity = commentController.addComment(mockAddCommentRequestBody);

        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, stringResponseEntity.getStatusCode());
        assertEquals(invalidRequest, stringResponseEntity.getBody());

        verify(mockCommentService).addComment(mockAddCommentRequestBody);
        UnableToAddCommentInDbException unableToAddCommentInDbException = assertThrows(
                UnableToAddCommentInDbException.class,
                () -> mockCommentService.addComment(mockAddCommentRequestBody)
        );
        assertEquals(exceptionMessage, unableToAddCommentInDbException.getMessage());
    }

    @Test
    public void shouldNotAddCommentWhenMessageIsEmptyOrNull() throws InvalidUserNameException,
            UserNotInDbException, UnableToAddUserToDbException,
            UnableToAddCommentInDbException, EmptyMessageException {
        //arrange
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        CommentService mockCommentService = mock(CommentService.class);
        Comment mockComment = mock(Comment.class);
        String exceptionMessage = "Exception";
        when(mockCommentService.addComment(mockAddCommentRequestBody)).
                thenThrow(new EmptyMessageException(exceptionMessage));
        ResponseBodyError invalidRequest = new ResponseBodyError();
        invalidRequest.setMessage("Invalid Request");
        invalidRequest.setError(exceptionMessage);

        // act
        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<Object> stringResponseEntity = commentController.addComment(mockAddCommentRequestBody);

        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, stringResponseEntity.getStatusCode());
        assertEquals(invalidRequest, stringResponseEntity.getBody());

        verify(mockCommentService).addComment(mockAddCommentRequestBody);
        EmptyMessageException emptyMessageException = assertThrows(
                EmptyMessageException.class,
                () -> mockCommentService.addComment(mockAddCommentRequestBody)
        );
        assertEquals(exceptionMessage, emptyMessageException.getMessage());
    }

    @Test
    public void shouldNotAddCommentWhenPostedForUnableToAddInDb() throws InvalidUserNameException,
            UserNotInDbException, UnableToAddUserToDbException,
            UnableToAddCommentInDbException, EmptyMessageException {
        //arrange
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        CommentService mockCommentService = mock(CommentService.class);
        Comment mockComment = mock(Comment.class);
        String exceptionMessage = "Exception";
        when(mockCommentService.addComment(mockAddCommentRequestBody)).
                thenThrow(new UnableToAddUserToDbException(exceptionMessage));
        ResponseBodyError invalidRequest = new ResponseBodyError();
        invalidRequest.setMessage("Invalid Request");
        invalidRequest.setError(exceptionMessage);

        // act
        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<Object> stringResponseEntity = commentController.addComment(mockAddCommentRequestBody);

        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, stringResponseEntity.getStatusCode());
        assertEquals(invalidRequest, stringResponseEntity.getBody());

        verify(mockCommentService).addComment(mockAddCommentRequestBody);
        UnableToAddUserToDbException unableToAddUserToDbException = assertThrows(
                UnableToAddUserToDbException.class, () -> mockCommentService.addComment(mockAddCommentRequestBody));
        assertEquals(exceptionMessage, unableToAddUserToDbException.getMessage());
    }

    @Test
    public void shouldAddComment() throws InvalidUserNameException, UserNotInDbException,
            UnableToAddUserToDbException, UnableToAddCommentInDbException,
            EmptyMessageException {
        // arrange
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        CommentService mockCommentService = mock(CommentService.class);
        Comment mockComment = mock(Comment.class);

        when(mockCommentService.addComment(mockAddCommentRequestBody)).thenReturn(mockComment);
        Object validRequest = mockComment;

        // act
        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<Object> stringResponseEntity = commentController.addComment(mockAddCommentRequestBody);

        // assert
        assertEquals(HttpStatus.CREATED, stringResponseEntity.getStatusCode());
        assertTrue(stringResponseEntity.getBody() instanceof AddCommentResponseBodySuccess);

        verify(mockCommentService).addComment(mockAddCommentRequestBody);
        Comment comment = mockCommentService.addComment(mockAddCommentRequestBody);
        assertEquals(mockComment, comment);
    }

    @Test
    public void shouldNotShowCommentsWhenInvalidRequest() throws UserNotInDbException, InvalidRequestException {
        CommentService mockCommentService = mock(CommentService.class);
        String exceptionMessage = "Exception";
        when(mockCommentService.showComments(1, Long.getLong("1"), new User())).thenThrow(
                new InvalidRequestException(exceptionMessage)
        );
        ResponseBodyError invalidRequest = new ResponseBodyError();
        invalidRequest.setMessage("Invalid Request");
        invalidRequest.setError(exceptionMessage);

        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<Object> objectResponseEntity = commentController.showComments(Long.getLong("1"), 1, new User());

        assertTrue(objectResponseEntity.getBody() instanceof ResponseBodyError);
        assertEquals(HttpStatus.BAD_REQUEST, objectResponseEntity.getStatusCode());

        InvalidRequestException invalidRequestException = assertThrows(
                InvalidRequestException.class,
                () -> mockCommentService.showComments(1, Long.getLong("1"), new User())
        );
        assertEquals(exceptionMessage, invalidRequestException.getMessage());
    }

    @Test
    public void shouldNotShowCommentsWhenUserNotInDbException() throws UserNotInDbException, InvalidRequestException {
        CommentService mockCommentService = mock(CommentService.class);
        String exceptionMessage = "Exception";
        when(mockCommentService.showComments(1, Long.getLong("1"), new User())).thenThrow(
                new UserNotInDbException(exceptionMessage)
        );
        ResponseBodyError invalidRequest = new ResponseBodyError();
        invalidRequest.setMessage("Invalid Request");
        invalidRequest.setError(exceptionMessage);

        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<Object> objectResponseEntity = commentController.showComments(Long.getLong("1"), 1, new User());

        assertTrue(objectResponseEntity.getBody() instanceof ResponseBodyError);
        assertEquals(HttpStatus.BAD_REQUEST, objectResponseEntity.getStatusCode());

        UserNotInDbException userNotInDbException = assertThrows(
                UserNotInDbException.class,
                () -> mockCommentService.showComments(1, Long.getLong("1"), new User())
        );
        assertEquals(exceptionMessage, userNotInDbException.getMessage());
    }

    @Test
    public void shouldNotShowCommentsWhenInvalidPageNumber() throws UserNotInDbException, InvalidRequestException {
        CommentService mockCommentService = mock(CommentService.class);

        String exceptionMessage = "Exception";

        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getName()).thenReturn("name");

        UserService mockUserService = mock(UserService.class);
        when(mockUserService.isValidUserId(1L)).thenReturn(mockUser);

        when(mockCommentService.showComments(-1, 1L, mockUser)).thenThrow(
                new InvalidRequestException(exceptionMessage)
        );

        ResponseBodyError invalidRequest = new ResponseBodyError();
        invalidRequest.setMessage("Invalid Request");
        invalidRequest.setError(exceptionMessage);

        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<Object> objectResponseEntity = commentController.showComments(1L, -1, mockUser);

        assertTrue(objectResponseEntity.getBody() instanceof ResponseBodyError);
        assertEquals(HttpStatus.BAD_REQUEST, objectResponseEntity.getStatusCode());

        InvalidRequestException invalidRequestException = assertThrows(
                InvalidRequestException.class,
                () -> mockCommentService.showComments(-1, 1L, mockUser)
        );
        assertEquals(exceptionMessage, invalidRequestException.getMessage());
    }

    @Test
    public void shouldShowComments() throws UserNotInDbException, InvalidRequestException {
        CommentService mockCommentService = mock(CommentService.class);

        String exceptionMessage = "Exception";

        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getName()).thenReturn("name");

        UserService mockUserService = mock(UserService.class);
        when(mockUserService.isValidUserId(1L)).thenReturn(mockUser);

        List<Comment> expectedComments = Arrays.asList(
                new Comment(1L, "Test Comment 1", Timestamp.from(Instant.now()), mockUser.getId(), mockUser.getId()),
                new Comment(2L, "Test Comment 2", Timestamp.from(Instant.now()), mockUser.getId(), mockUser.getId())
        );
        when(mockCommentService.showComments(1, 1L, mockUser)).thenReturn(
                expectedComments
        );

        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<Object> objectResponseEntity = commentController.showComments(1L, 1, mockUser);

        assertFalse(objectResponseEntity.getBody() instanceof ResponseBodyError);
        assertEquals(HttpStatus.OK, objectResponseEntity.getStatusCode());
        assertEquals(expectedComments, objectResponseEntity.getBody());
    }
}