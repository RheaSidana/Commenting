package org.vijayi.commenting.comment.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

import java.util.List;

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
            ResponseBodyError addCommentResponseBody = new ResponseBodyError();
            addCommentResponseBody.setMessage("Invalid Request");
            addCommentResponseBody.setError(ex.getMessage());
            return ResponseEntity.status(
                    HttpStatus.BAD_REQUEST
            ).body(
                    addCommentResponseBody
            );
        } catch (UserNotInDbException ex) {
            ResponseBodyError addCommentResponseBody = new ResponseBodyError();
            addCommentResponseBody.setMessage("Invalid Request");
            addCommentResponseBody.setError(ex.getMessage());
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body(
                    addCommentResponseBody
            );
        } catch (UnableToAddUserToDbException ex) {
            ResponseBodyError addCommentResponseBody = new ResponseBodyError();
            addCommentResponseBody.setMessage("Invalid Request");
            addCommentResponseBody.setError(ex.getMessage());
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body(
                    addCommentResponseBody
            );
        } catch (UnableToAddCommentInDbException ex) {
            ResponseBodyError addCommentResponseBody = new ResponseBodyError();
            addCommentResponseBody.setMessage("Invalid Request");
            addCommentResponseBody.setError(ex.getMessage());
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body(
                    addCommentResponseBody
            );
        } catch(EmptyMessageException ex){
            ResponseBodyError addCommentResponseBody = new ResponseBodyError();
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

    @GetMapping("/comment")
    public ResponseEntity<Object> showComments(
            @RequestParam(name = "id") Long id,
            @RequestParam(name="page") int page,
            @RequestBody User loggedInUser
    ){
        List<Comment> commentsOfPostedFor = null;
        try{
            commentsOfPostedFor = commentService.showComments(page, id, loggedInUser);
        } catch (InvalidRequestException ex){
            ResponseBodyError responseBodyError = new ResponseBodyError();
            responseBodyError.setMessage("Invalid Request");
            responseBodyError.setError(ex.getMessage());
            return ResponseEntity.status(
                    HttpStatus.BAD_REQUEST
            ).body(
                    responseBodyError
            );
        } catch(UserNotInDbException ex){
            ResponseBodyError responseBodyError = new ResponseBodyError();
            responseBodyError.setMessage("Invalid Request");
            responseBodyError.setError(ex.getMessage());
            return ResponseEntity.status(
                    HttpStatus.BAD_REQUEST
            ).body(
                    responseBodyError
            );
        }
        catch (Exception ex) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseBodyError()
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                commentsOfPostedFor
        );
    }
}
