package org.vijayi.commenting.comment.view.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.vijayi.commenting.user.repository.model.User;

import java.util.Objects;

public class AddCommentRequestBody {
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private User postedBy;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private User postedFor;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String message;

    public AddCommentRequestBody(User postedBy, User postedFor, String message) {
        this.postedBy = postedBy;
        this.postedFor = postedFor;
        this.message = message;
    }

    public AddCommentRequestBody() {
    }

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    public User getPostedFor() {
        return postedFor;
    }

    public void setPostedFor(User postedFor) {
        this.postedFor = postedFor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddCommentRequestBody that = (AddCommentRequestBody) o;
        return Objects.equals(postedBy, that.postedBy) && Objects.equals(postedFor, that.postedFor) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postedBy, postedFor, message);
    }

    @Override
    public String toString() {
        return "AddCommentRequestBody{" +
                "postedBy=" + postedBy +
                ", postedFor=" + postedFor +
                ", message='" + message + '\'' +
                '}';
    }
}
