package org.vijayi.commenting.comment.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.vijayi.commenting.user.repository.model.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @JsonProperty
    private Long id;

    @Column(nullable = false)
    @JsonProperty
    private String message;

    @Column(
            name = "Date_Time",
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
    )
    @JsonProperty
    private Timestamp dateTime;

    @ManyToOne
    @JoinColumn(name = "POSTED_BY", referencedColumnName = "ID", insertable = false, updatable = false)
    private User postedBy;

    @ManyToOne
    @JoinColumn(name = "POSTED_FOR", referencedColumnName = "ID", insertable = false, updatable = false)
    private User postedFor;

    public Comment(Long id, String message, Timestamp dateTime, User postedBy, User postedFor) {
        this.id = id;
        this.message = message;
        this.dateTime = dateTime;
        this.postedBy = postedBy;
        this.postedFor = postedFor;
    }

    public Comment() {
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) && Objects.equals(message, comment.message) && Objects.equals(dateTime, comment.dateTime) && Objects.equals(postedBy, comment.postedBy) && Objects.equals(postedFor, comment.postedFor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, dateTime, postedBy, postedFor);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", dateTime=" + dateTime +
                ", postedBy=" + postedBy +
                ", postedFor=" + postedFor +
                '}';
    }
}
