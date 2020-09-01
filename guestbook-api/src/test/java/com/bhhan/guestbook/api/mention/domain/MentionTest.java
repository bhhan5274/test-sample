package com.bhhan.guestbook.api.mention.domain;

import com.bhhan.guestbook.api.mention.service.dto.MentionDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

/**
 * Created by hbh5274@gmail.com on 2020-09-01
 * Github : http://github.com/bhhan5274
 */
public class MentionTest {
    public static Mention getMentionFixture(Long id){
        final Mention mention = Mockito.mock(Mention.class);
        given(mention.getId()).willReturn(id);
        given(mention.getName()).willReturn("name");
        given(mention.getContent()).willReturn("content");
        given(mention.getCreatedAt()).willReturn(now());

        return mention;
    }

    public static MentionDto.MentionResponse getMentionResponseFixture(Long id){
        return MentionDto.MentionResponse.of(getMentionFixture(id));
    }

    @Test
    void builder_ValidInput_ValidOutput(){
        final Mention mention = Mention.builder()
                .name("name")
                .content("content")
                .build();
        then(mention)
                .hasFieldOrPropertyWithValue("name", "name")
                .hasFieldOrPropertyWithValue("content", "content");
    }

    @Test
    void updateMention_ValidInput_MentionUpdated(){
        final Mention mention = Mention.builder().name("name")
                .content("content")
                .build();

        final String updatedName = "updated name";
        final String updatedContent = "updated content";

        mention.update(updatedName, updatedContent);

        then(mention)
                .hasFieldOrPropertyWithValue("name", updatedName)
                .hasFieldOrPropertyWithValue("content", updatedContent);
    }
}