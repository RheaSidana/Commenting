package org.vijayi.commenting.comment.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.vijayi.commenting.comment.repository.model.Comment;
import org.vijayi.commenting.comment.service.CommentService;
import org.vijayi.commenting.comment.view.model.request.AddCommentRequestBody;
import org.vijayi.commenting.user.exceptions.InvalidUserNameException;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    public CommentController() {
    }

    @PostMapping("/comment")
    public ResponseEntity<String> addComment(@RequestBody AddCommentRequestBody addCommentRequestBody){
        try {
            Comment comment = commentService.addComment(addCommentRequestBody);
        } catch (InvalidUserNameException ex) {
//            return "Invalid Request";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request"+"\n Error: "+ ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Comment Added");
    }
}
