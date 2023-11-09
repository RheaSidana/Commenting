package org.vijayi.commenting.comment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vijayi.commenting.comment.repository.CommentRepository;
import org.vijayi.commenting.comment.repository.model.Comment;
import org.vijayi.commenting.comment.view.model.request.AddCommentRequestBody;
import org.vijayi.commenting.user.exceptions.InvalidUserNameException;
import org.vijayi.commenting.user.service.UserService;

import javax.transaction.Transactional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    public CommentService(UserService userService) {
        this.userService = userService;
    }

    public CommentService() {
    }

    @Transactional
    public Comment addComment(AddCommentRequestBody addCommentRequestBody) throws InvalidUserNameException {
        boolean isValidUserName_PostedBy = userService.isValidUserName(addCommentRequestBody.getPostedBy());

        if(!isValidUserName_PostedBy) {
            throw new InvalidUserNameException("PostedBy: invalid name provided");
        }
//        userService.isAvailableInDb(addCommentRequestBody.getPostedBy());
        return new Comment();
    }
}
