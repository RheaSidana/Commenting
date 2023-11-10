package org.vijayi.commenting.comment.view.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.vijayi.commenting.comment.repository.model.Comment;
import org.vijayi.commenting.user.repository.model.User;

import java.util.Objects;

public class AddCommentResponseBodySuccess {
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private Long id;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private User postedBy;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private User postedFor;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String message;

    public AddCommentResponseBodySuccess(Comment comment){
        this.id = comment.getId();
        this.message = comment.getMessage();
        this.postedBy = comment.getUserPostedBy();
        this.postedFor = comment.getUserPostedFor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddCommentResponseBodySuccess that = (AddCommentResponseBodySuccess) o;
        return Objects.equals(id, that.id) && Objects.equals(postedBy, that.postedBy) && Objects.equals(postedFor, that.postedFor) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, postedBy, postedFor, message);
    }

    @Override
    public String toString() {
        return "AddCommentResponseBodySuccess{" + "\n "+
                "id=" + id +
                "," + "\n"+" postedBy=" + postedBy +
                "," + "\n"+" postedFor=" + postedFor +
                "," + "\n"+" message='" + message + '\'' +
                '}';
    }
}
