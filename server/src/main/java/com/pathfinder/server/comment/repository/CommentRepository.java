package com.pathfinder.server.comment.repository;

import com.pathfinder.server.comment.entity.Comment;
import com.pathfinder.server.scrap.entity.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByDiaryDiaryId(Long diaryId, Pageable pageable);
}
