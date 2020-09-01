package com.bhhan.guestbook.api.mention.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by hbh5274@gmail.com on 2020-09-01
 * Github : http://github.com/bhhan5274
 */
public interface MentionRepository extends JpaRepository<Mention, Long> {
    List<Mention> findAllByOrderByCreatedAtDesc();
}
