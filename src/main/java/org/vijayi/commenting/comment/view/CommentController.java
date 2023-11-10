package org.vijayi.commenting.comment.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<Object> addComment(@RequestBody AddCommentRequestBody addCommentRequestBody) {
        Comment comment;
        try {
            comment = commentService.addComment(addCommentRequestBody);
        } catch (InvalidUserNameException ex) {
            AddCommentResponseBodyError addCommentResponseBody = new AddCommentResponseBodyError();
            addCommentResponseBody.setMessage("Invalid Request");
            addCommentResponseBody.setError(ex.getMessage());
            return ResponseEntity.status(
                    HttpStatus.BAD_REQUEST
            ).body(
                    addCommentResponseBody
            );
        } catch (UserNotInDbException ex) {
            AddCommentResponseBodyError addCommentResponseBody = new AddCommentResponseBodyError();
            addCommentResponseBody.setMessage("Invalid Request");
            addCommentResponseBody.setError(ex.getMessage());
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body(
                    addCommentResponseBody
            );
        } catch (UnableToAddUserToDbException ex) {
            AddCommentResponseBodyError addCommentResponseBody = new AddCommentResponseBodyError();
            addCommentResponseBody.setMessage("Invalid Request");
            addCommentResponseBody.setError(ex.getMessage());
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body(
                    addCommentResponseBody
            );
        } catch (UnableToAddCommentInDbException ex) {
            AddCommentResponseBodyError addCommentResponseBody = new AddCommentResponseBodyError();
            addCommentResponseBody.setMessage("Invalid Request");
            addCommentResponseBody.setError(ex.getMessage());
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body(
                    addCommentResponseBody
            );
        } catch(EmptyMessageException ex){
            AddCommentResponseBodyError addCommentResponseBody = new AddCommentResponseBodyError();
            addCommentResponseBody.setMessage("Invalid Request");
            addCommentResponseBody.setError(ex.getMessage());
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body(
                    addCommentResponseBody
            );
        }

        return ResponseEntity.status(
                HttpStatus.CREATED
        ).body(
                new AddCommentResponseBodySuccess(comment)
        );
    }
}
