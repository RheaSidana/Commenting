package org.vijayi.commenting.comment.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.vijayi.commenting.comment.repository.model.Comment;
import org.vijayi.commenting.comment.service.CommentService;
import org.vijayi.commenting.comment.view.model.request.AddCommentRequestBody;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String addComment(@RequestBody AddCommentRequestBody addCommentRequestBody){
        return "Comment added";
    }
}
