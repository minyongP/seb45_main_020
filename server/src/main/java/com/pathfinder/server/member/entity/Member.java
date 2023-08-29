package com.pathfinder.server.member.entity;

import com.pathfinder.server.recommend.entity.Recommend;
import com.pathfinder.server.thread.entity.Thread;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(length = 30, nullable = false, updatable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false)
    private String introduce = "안녕하세요.";

//    @Column(nullable = false)
//    private String profileImageUrl = "http://localhost:8080/images/sample.jpg";
//
//    @ElementCollection(fetch = FetchType.EAGER) // 인가
//    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Recommend> recommends = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Thread> threads = new ArrayList<>();

    public void setRecommend(Recommend recommend) {
        recommends.add(recommend);
        if (recommend.getMember() != this) {
            recommend.setMember(this);
        }
    }

    public void setThread(Thread thread) {
        threads.add(thread);
        if (thread.getMember() != this) {
            thread.setMember(this);
        }
    }
}