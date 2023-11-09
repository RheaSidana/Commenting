package org.vijayi.commenting.comment.view;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.vijayi.commenting.comment.repository.model.Comment;
import org.vijayi.commenting.comment.service.CommentService;
import org.vijayi.commenting.comment.view.model.request.AddCommentRequestBody;
import org.vijayi.commenting.user.exceptions.InvalidUserNameException;
import org.vijayi.commenting.user.exceptions.UserNotInDbException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommentControllerTest {
    @Test
    public void shouldNotAddCommentWhenInvalidUserNameException() throws InvalidUserNameException, UserNotInDbException {
        // arrange
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        CommentService mockCommentService = mock(CommentService.class);
        String exceptionMessage = "Exception";
        when(mockCommentService.addComment(mockAddCommentRequestBody)).
                thenThrow(new InvalidUserNameException(exceptionMessage));
        String invalidRequest = "Invalid Request\n" + " Error: Exception";

        //act
        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<String> stringResponseEntity = commentController.addComment(mockAddCommentRequestBody);

        // assert
        assertEquals(HttpStatus.BAD_REQUEST, stringResponseEntity.getStatusCode());
        assertEquals(invalidRequest, stringResponseEntity.getBody());

        verify(mockCommentService).addComment(mockAddCommentRequestBody);
        InvalidUserNameException invalidUserNameException = assertThrows(
                InvalidUserNameException.class, () -> mockCommentService.addComment(mockAddCommentRequestBody));
        assertEquals(exceptionMessage, invalidUserNameException.getMessage());
    }

    @Test
    public void shouldNotAddCommentWhenPostedByNotFoundInDb() throws InvalidUserNameException, UserNotInDbException {
        //arrange
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        CommentService mockCommentService = mock(CommentService.class);
        Comment mockComment = mock(Comment.class);
        String exceptionMessage = "Exception";
        when(mockCommentService.addComment(mockAddCommentRequestBody)).
                thenThrow(new UserNotInDbException(exceptionMessage));
        String invalidRequest = "Invalid Request\n" + " Error: Exception";

        // act
        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<String> stringResponseEntity = commentController.addComment(mockAddCommentRequestBody);

        //assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, stringResponseEntity.getStatusCode());
        assertEquals(invalidRequest, stringResponseEntity.getBody());

        verify(mockCommentService).addComment(mockAddCommentRequestBody);
        UserNotInDbException userNotInDbException = assertThrows(
                UserNotInDbException.class, () -> mockCommentService.addComment(mockAddCommentRequestBody));
        assertEquals(exceptionMessage, userNotInDbException.getMessage());
    }

    @Test
    public void shouldAddComment() throws InvalidUserNameException, UserNotInDbException {
        // arrange
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        CommentService mockCommentService = mock(CommentService.class);
        Comment mockComment = mock(Comment.class);
        when(mockCommentService.addComment(mockAddCommentRequestBody)).thenReturn(mockComment);
        String invalidRequest = "Comment Added";

        // act
        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<String> stringResponseEntity = commentController.addComment(mockAddCommentRequestBody);

        // assert
        assertEquals(HttpStatus.CREATED, stringResponseEntity.getStatusCode());
        assertEquals(invalidRequest, stringResponseEntity.getBody());

        verify(mockCommentService).addComment(mockAddCommentRequestBody);
        Comment comment = mockCommentService.addComment(mockAddCommentRequestBody);
        assertEquals(mockComment, comment);
    }
}