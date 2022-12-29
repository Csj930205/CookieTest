package com.example.aoptest.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String id;

    private String name;

    @Column(name = "count_user", columnDefinition = "int default '0' ")
    private int countUser;

    @Transient
    private boolean delYn;

    @Builder
    public User(Long seq, String id, String name, int countUser, boolean delYn) {
        this.seq = seq;
        this.id = id;
        this.name = name;
        this.countUser = countUser;
        this.delYn = delYn;
    }

    public void countUp(int countUser) {
        this.countUser = countUser + 1;
    }

    public void countDown(int countUser) {
        this.countUser = countUser - 1;
    }

    public void delete() {
        this.delYn = true;
    }

    public void notDelete() {
        this.delYn = false;
    }
}
