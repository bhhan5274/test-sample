package com.bhhan.guestbook.api.mention.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by hbh5274@gmail.com on 2020-09-01
 * Github : http://github.com/bhhan5274
 */

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MENTIONS")
public class Mention {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENTION_ID")
    private Long id;

    private String name;
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Mention(Long id, String name, String content){
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public void update(String name, String content){
        this.name = name;
        this.content = content;
    }
}
