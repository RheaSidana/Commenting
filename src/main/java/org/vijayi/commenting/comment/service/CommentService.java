package org.vijayi.commenting.comment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.vijayi.commenting.comment.exceptions.EmptyMessageException;
import org.vijayi.commenting.comment.exceptions.InvalidRequestException;
import org.vijayi.commenting.comment.exceptions.UnableToAddCommentInDbException;
import org.vijayi.commenting.comment.repository.CommentRepository;
import org.vijayi.commenting.comment.repository.model.Comment;
import org.vijayi.commenting.comment.view.model.request.AddCommentRequestBody;
import org.vijayi.commenting.user.exceptions.InvalidUserNameException;
import org.vijayi.commenting.user.exceptions.UnableToAddUserToDbException;
import org.vijayi.commenting.user.exceptions.UserNotInDbException;
import org.vijayi.commenting.user.repository.model.User;
import org.vijayi.commenting.user.service.UserService;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

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
    public Comment addComment(AddCommentRequestBody addCommentRequestBody) throws InvalidUserNameException,
            UserNotInDbException, UnableToAddUserToDbException,
            UnableToAddCommentInDbException, EmptyMessageException {
        isValidPostedByUserName(addCommentRequestBody);
        User postedBy = getPostedByUser(addCommentRequestBody);

        isValidPostedForUserName(addCommentRequestBody);
        User postedFor = getPostedForUser(addCommentRequestBody);

        boolean messageEmpty = isMessageEmpty(addCommentRequestBody);
        if(messageEmpty){
            return new Comment();
        }

        Comment comment = newComment(
                addCommentRequestBody.getMessage(),
                postedBy,
                postedFor
        );

        return addToDb(comment);
    }

    public List<Comment> showComments(int page, Long id, User loggedInUser) throws InvalidRequestException,
            UserNotInDbException {
        isValidRequest(id, loggedInUser);
        isValidUser(id, loggedInUser);
        isPageValid(page);

        List<Comment> comments = fetchAllComments(id, page);
        return comments;
    }

    private boolean isPageValid(int page) throws InvalidRequestException {
        if(page <= 0){
            throw new InvalidRequestException("Invalid Page number provided");
        }
        return true;
    }

    private List<Comment> fetchAllComments(Long id, int page) {
        int size = 2;
//        int size = 5;
        Sort sort = Sort.by("dateTime").descending();
        Pageable pageable = PageRequest.of(page-1, size, sort);
        Page<Comment> pageComments = commentRepository.findAllByPostedFor(id, pageable);

        return pageComments.getContent();
    }

    private boolean isValidUser(Long id, User loggedInUser) throws UserNotInDbException {
        User validUser = userService.isValidUserId(id);
        if(!validUser.equals(loggedInUser)){
            throw new UserNotInDbException("User is not present in the db.");
        }

        return false;
    }

    private boolean isValidRequest(Long id, User loggedInUser) throws InvalidRequestException {
        if(id != loggedInUser.getId()){
            throw new InvalidRequestException("User does not have access to comments.");
        }
        return true;
    }

    private Comment newComment(String message, User postedBy, User postedFor){
        Comment comment = new Comment();
        comment.setPostedBy(postedBy.getId());
        comment.setUserPostedBy(postedBy);
        comment.setPostedFor(postedFor.getId());
        comment.setUserPostedFor(postedFor);
        comment.setMessage(message);
        comment.setDateTime(Timestamp.from(Instant.now()));

        return comment;
    }

    private boolean isMessageEmpty(AddCommentRequestBody addCommentRequestBody) throws EmptyMessageException {
        if (addCommentRequestBody.getMessage() == null ||
                addCommentRequestBody.getMessage().isEmpty() ||
                addCommentRequestBody.getMessage().isBlank()) {
            throw new EmptyMessageException("Message is empty or null.");
        }

        return false;
    }

    private User getPostedForUser(AddCommentRequestBody addCommentRequestBody) throws UnableToAddUserToDbException {
        User user = addCommentRequestBody.getPostedFor();
        boolean availableInDbPostedFor = userService.isAvailableInDb(user);
        if(!availableInDbPostedFor){
            user = userService.addUserToDb(user);
            if(user == null){
                throw new UnableToAddUserToDbException("PostedFor: unable to add user to db");
            }
        }

        return user;
    }

    private User getPostedByUser(AddCommentRequestBody addCommentRequestBody) throws UserNotInDbException {
        isPostedByUserPresentInDb(addCommentRequestBody);

        return addCommentRequestBody.getPostedBy();
    }

    private void isValidPostedForUserName(AddCommentRequestBody addCommentRequestBody) throws UserNotInDbException, InvalidUserNameException {
        boolean validUserNamePostedFor = userService.isValidUserName(addCommentRequestBody.getPostedFor());
        if(!validUserNamePostedFor){
            throw new InvalidUserNameException("PostedFor: invalid name provided");
        }
    }

    private void isPostedByUserPresentInDb(AddCommentRequestBody addCommentRequestBody) throws UserNotInDbException {
        boolean availableInDbPostedBy = userService.isAvailableInDb(addCommentRequestBody.getPostedBy());
        if(!availableInDbPostedBy) {
            throw new UserNotInDbException("PostedBy: user not found");
        }
    }

    private void isValidPostedByUserName(AddCommentRequestBody addCommentRequestBody) throws InvalidUserNameException {
        boolean isValidUserNamePostedBy = userService.isValidUserName(addCommentRequestBody.getPostedBy());
        if(!isValidUserNamePostedBy) {
            throw new InvalidUserNameException("PostedBy: invalid name provided");
        }
    }

    private Comment addToDb(Comment comment) throws UnableToAddCommentInDbException {
        Comment commentInDB = commentRepository.save(comment);
        if(commentInDB == null){
            throw new UnableToAddCommentInDbException("failed to add comment in db");
        }
        return commentInDB;
    }
}
