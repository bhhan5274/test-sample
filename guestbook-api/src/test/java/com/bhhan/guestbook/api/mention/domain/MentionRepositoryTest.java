package com.bhhan.guestbook.api.mention.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * Created by hbh5274@gmail.com on 2020-09-01
 * Github : http://github.com/bhhan5274
 */

@DataJpaTest
class MentionRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MentionRepository mentionRepository;

    @Test
    public void findAllMentions_ValidInput_MentionsOrderByCreatedAtDesc(){
        final int sizeOfMentions = 1 << 8;

        IntStream.range(0, sizeOfMentions).forEach(i -> {
            testEntityManager.persist(Mention.builder().name("name" + i).content("content" + i).build());
        });

        final List<Mention> mentions = mentionRepository.findAllByOrderByCreatedAtDesc();
        then(mentions).hasSize(sizeOfMentions);

        for(int i = 1; i < sizeOfMentions; i++){
            then(mentions.get(i - 1).getCreatedAt()).isAfterOrEqualTo(mentions.get(i).getCreatedAt());
        }
    }
}