package org.vijayi.commenting.comment.service;

import org.junit.jupiter.api.Test;
import org.vijayi.commenting.comment.view.model.request.AddCommentRequestBody;
import org.vijayi.commenting.user.exceptions.InvalidUserNameException;
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

        InvalidUserNameException invalidUserNameException = assertThrows(InvalidUserNameException.class, () -> new CommentService(mockUserService).addComment(mockAddCommentRequestBody));
        assertEquals("PostedBy: invalid name provided", invalidUserNameException.getMessage());

        verify(mockUserService).isValidUserName(mockAddCommentRequestBody.getPostedBy());
        assertFalse(mockUserService.isValidUserName(mockUser));
    }

    @Test
    public void shouldAddCommentWhenUserNotInDb() throws InvalidUserNameException, UserNotInDbException {
        User mockUser = mock(User.class);
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        UserService mockUserService = mock(UserService.class);
        when(mockAddCommentRequestBody.getPostedBy()).thenReturn(mockUser);
        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedBy())).thenReturn(false);

        verify(mockAddCommentRequestBody, times(2)).getPostedBy();
        assertEquals(mockUser, mockAddCommentRequestBody.getPostedBy());

//        CommentService commentService = new CommentService(mockUserService);
//        commentService.addComment(mockAddCommentRequestBody);

        UserNotInDbException userNotInDbException = assertThrows(UserNotInDbException.class, () -> new CommentService(mockUserService).addComment(mockAddCommentRequestBody));
        System.out.println(userNotInDbException.getMessage());

        verify(mockUserService).isValidUserName(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isValidUserName(mockUser));

        verify(mockUserService).isAvailableInDb(mockAddCommentRequestBody.getPostedBy());
        assertFalse(mockUserService.isAvailableInDb(mockUser));
    }

    @Test
    public void shouldAddComment() throws InvalidUserNameException, UserNotInDbException {
        User mockUser = mock(User.class);
        AddCommentRequestBody mockAddCommentRequestBody = mock(AddCommentRequestBody.class);
        UserService mockUserService = mock(UserService.class);
        when(mockAddCommentRequestBody.getPostedBy()).thenReturn(mockUser);
        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isAvailableInDb(mockAddCommentRequestBody.getPostedBy())).thenReturn(true);
        when(mockUserService.isValidUserName(mockAddCommentRequestBody.getPostedFor())).thenReturn(true);

        verify(mockAddCommentRequestBody, times(2)).getPostedBy();
        assertEquals(mockUser, mockAddCommentRequestBody.getPostedBy());

        CommentService commentService = new CommentService(mockUserService);
        commentService.addComment(mockAddCommentRequestBody);

        verify(mockUserService).isValidUserName(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isValidUserName(mockUser));

        verify(mockUserService).isAvailableInDb(mockAddCommentRequestBody.getPostedBy());
        assertTrue(mockUserService.isAvailableInDb(mockUser));
    }


}