package org.vijayi.commenting.comment.view;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.vijayi.commenting.comment.exceptions.EmptyMessageException;
import org.vijayi.commenting.comment.exceptions.UnableToAddCommentInDbException;
import org.vijayi.commenting.comment.repository.model.Comment;
import org.vijayi.commenting.comment.service.CommentService;
import org.vijayi.commenting.comment.view.model.request.AddCommentRequestBody;
import org.vijayi.commenting.comment.view.model.response.AddCommentResponseBodyError;
import org.vijayi.commenting.comment.view.model.response.AddCommentResponseBodySuccess;
import org.vijayi.commenting.user.exceptions.InvalidUserNameException;
import org.vijayi.commenting.user.exceptions.UnableToAddUserToDbException;
import org.vijayi.commenting.user.exceptions.UserNotInDbException;

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
        AddCommentResponseBodyError invalidRequest = new AddCommentResponseBodyError();
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
        AddCommentResponseBodyError invalidRequest = new AddCommentResponseBodyError();
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
        AddCommentResponseBodyError invalidRequest = new AddCommentResponseBodyError();
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
        AddCommentResponseBodyError invalidRequest = new AddCommentResponseBodyError();
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
        AddCommentResponseBodyError invalidRequest = new AddCommentResponseBodyError();
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
    public void shouldAddComment() throws InvalidUserNameException, UserNotInDbException, UnableToAddUserToDbException, UnableToAddCommentInDbException, EmptyMessageException {
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
}