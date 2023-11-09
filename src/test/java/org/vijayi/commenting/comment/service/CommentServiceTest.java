package org.vijayi.commenting.comment.service;

import org.junit.jupiter.api.Test;
import org.vijayi.commenting.comment.view.model.request.AddCommentRequestBody;
import org.vijayi.commenting.user.exceptions.InvalidUserNameException;
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

        InvalidUserNameException invalidUserNameException = assertThrows(InvalidUserNameException.class, () -> new CommentService(mockUserService).addComment(mockAddCommentRequestBody));
        assertEquals("PostedBy: invalid name provided", invalidUserNameException.getMessage());

        verify(mockUserService).isValidUserName(mockAddCommentRequestBody.getPostedBy());
        assertFalse(mockUserService.isValidUserName(mockUser));
    }

    @Test
    public void shouldAddComment() throws InvalidUserNameException {
        User mockUser = mock(User.class);
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        UserService mockUserService = mock(UserService.class);
        when(mockAddCommentRequestBody.getPostedBy()).thenReturn(mockUser);
        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);

        verify(mockAddCommentRequestBody).getPostedBy();
        assertEquals(mockUser, mockAddCommentRequestBody.getPostedBy());

        CommentService commentService = new CommentService(mockUserService);
        commentService.addComment(mockAddCommentRequestBody);

        verify(mockUserService).isValidUserName(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isValidUserName(mockUser));
    }


}