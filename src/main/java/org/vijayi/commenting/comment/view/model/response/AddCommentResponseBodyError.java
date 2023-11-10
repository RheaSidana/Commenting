package org.vijayi.commenting.comment.view.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AddCommentResponseBodyError {
    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String message;

    @JsonProperty
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String error;

    public AddCommentResponseBodyError(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public AddCommentResponseBodyError() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddCommentResponseBodyError that = (AddCommentResponseBodyError) o;
        return Objects.equals(message, that.message) && Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, error);
    }

    @Override
    public String toString() {
        return "AddCommentResponseBody{" +
                "message='" + message + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
