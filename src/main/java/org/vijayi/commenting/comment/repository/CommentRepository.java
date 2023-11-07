package org.vijayi.commenting.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vijayi.commenting.comment.repository.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
