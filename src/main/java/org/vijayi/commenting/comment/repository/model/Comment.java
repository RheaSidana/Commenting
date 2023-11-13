package org.vijayi.commenting.comment.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.vijayi.commenting.user.repository.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    @JsonProperty
    private Long id;

    @Column(name = "message")
    @NotNull(message = "message cannot be null")
    @JsonProperty
    private String message;

    @Column(
            name = "Date_Time",
            nullable = false
    )
    @CreationTimestamp
    @NotNull(message = "date_time cannot be null")
    @JsonProperty
    private Timestamp dateTime;

    @Column(name = "posted_by")
    @NotNull(message = "posted_by cannot be null")
    private Long postedBy;

    @ManyToOne
    @JoinColumn(name = "POSTED_BY", referencedColumnName = "ID", insertable = false, updatable = false)
    private User userPostedBy;

    @Column(name = "posted_for")
    @NotNull(message = "posted_for cannot be null")
    private Long postedFor;

    @ManyToOne
    @JoinColumn(name = "POSTED_FOR", referencedColumnName = "ID", insertable = false, updatable = false)
    private User userPostedFor;

    public User getUserPostedBy() {
        return userPostedBy;
    }

    public void setUserPostedBy(User userPostedBy) {
        this.userPostedBy = userPostedBy;
    }

    public User getUserPostedFor() {
        return userPostedFor;
    }

    public void setUserPostedFor(User userPostedFor) {
        this.userPostedFor = userPostedFor;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                "," + "\n" + " message='" + message + '\'' +
                "," + "\n" + " dateTime=" + dateTime +
                "," + "\n" + " postedBy=" + postedBy +
                "," + "\n" + " userPostedBy=" + userPostedBy +
                "," + "\n" + " postedFor=" + postedFor +
                "," + "\n" + " userPostedFor=" + userPostedFor +
                '}';
    }

    public Comment(Long id, String message, Timestamp dateTime, Long postedBy, Long postedFor) {
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

    public Long getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Long postedBy) {
        this.postedBy = postedBy;
    }

    public Long getPostedFor() {
        return postedFor;
    }

    public void setPostedFor(Long postedFor) {
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
        return Objects.hash(
                id,
                message,
                dateTime,
                postedBy,
                postedFor
        );
    }

}
