package com.pathfinder.server.comment.entity;

import com.pathfinder.server.diary.entity.Diary;
import com.pathfinder.server.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column
    private String content;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "DIARY_ID")
    private Diary diary;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

}
