package org.vijayi.commenting.comment.view;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.vijayi.commenting.comment.repository.model.Comment;
import org.vijayi.commenting.comment.service.CommentService;
import org.vijayi.commenting.comment.view.model.request.AddCommentRequestBody;
import org.vijayi.commenting.user.exceptions.InvalidUserNameException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommentControllerTest {
    @Test
    public void shouldNotAddCommentWhenInvalidUserNameException() throws InvalidUserNameException {
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        CommentService mockCommentService = mock(CommentService.class);
        String exceptionMessage = "Exception";
        when(mockCommentService.addComment(mockAddCommentRequestBody)).
                thenThrow(new InvalidUserNameException(exceptionMessage));
        String invalidRequest = "Invalid Request\n" + " Error: Exception";

        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<String> stringResponseEntity = commentController.addComment(mockAddCommentRequestBody);

        assertEquals(HttpStatus.BAD_REQUEST, stringResponseEntity.getStatusCode());
        assertEquals(invalidRequest, stringResponseEntity.getBody());

        verify(mockCommentService).addComment(mockAddCommentRequestBody);
        InvalidUserNameException invalidUserNameException = assertThrows(InvalidUserNameException.class, () -> mockCommentService.addComment(mockAddCommentRequestBody));
        assertEquals(exceptionMessage, invalidUserNameException.getMessage());
    }

    @Test
    public void shouldNotAddComment() throws InvalidUserNameException {
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        CommentService mockCommentService = mock(CommentService.class);
        Comment mockComment = mock(Comment.class);
        when(mockCommentService.addComment(mockAddCommentRequestBody)).thenReturn(mockComment);
        String invalidRequest = "Comment Added";

        CommentController commentController = new CommentController(mockCommentService);
        ResponseEntity<String> stringResponseEntity = commentController.addComment(mockAddCommentRequestBody);

        assertEquals(HttpStatus.CREATED, stringResponseEntity.getStatusCode());
        assertEquals(invalidRequest, stringResponseEntity.getBody());

        verify(mockCommentService).addComment(mockAddCommentRequestBody);
        Comment comment = mockCommentService.addComment(mockAddCommentRequestBody);
        assertEquals(mockComment, comment);
    }
}