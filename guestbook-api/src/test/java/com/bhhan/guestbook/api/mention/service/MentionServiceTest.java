package com.bhhan.guestbook.api.mention.service;

import com.bhhan.guestbook.api.mention.domain.Mention;
import com.bhhan.guestbook.api.mention.domain.MentionRepository;
import com.bhhan.guestbook.api.mention.service.dto.MentionDto;
import com.bhhan.guestbook.api.mention.service.exception.MentionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.bhhan.guestbook.api.mention.domain.MentionTest.getMentionFixture;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

/**
 * Created by hbh5274@gmail.com on 2020-09-01
 * Github : http://github.com/bhhan5274
 */

@ExtendWith(MockitoExtension.class)
class MentionServiceTest {
    private MentionService mentionService;

    @Mock
    private MentionRepository mentionRepository;

    @BeforeEach
    void setup(){
        mentionService = new MentionService(mentionRepository);
    }

    @Test
    void createMention_ValidInput_NoException(){
        final Mention mention = Mockito.mock(Mention.class);
        given(mention.getId()).willReturn(1L);
        given(mentionRepository.save(any(Mention.class))).willReturn(mention);

        final MentionDto.MentionRequest mentionRequest = MentionDto.MentionRequest.builder()
                .name("name")
                .content("content")
                .build();

        final Long id = mentionService.createMention(mentionRequest);
        then(id).isExactlyInstanceOf(Long.class);
    }

    @Test
    void readMention_ValidInput_MentionFound(){
        final Mention mention = getMentionFixture(1L);
        given(mentionRepository.findById(anyLong())).willReturn(Optional.of(mention));

        final MentionDto.MentionResponse foundMention = mentionService.readMention(1L);

        then(foundMention)
                .hasFieldOrPropertyWithValue("id", mention.getId())
                .hasFieldOrPropertyWithValue("name", mention.getName())
                .hasFieldOrPropertyWithValue("content", mention.getContent())
                .hasFieldOrPropertyWithValue("createdAt", mention.getCreatedAt());
    }

    @Test
    void readAllMention_ValidInput_MentionResponseList(){
        final List<Mention> mentions = Arrays.asList(
                getMentionFixture(1L),
                getMentionFixture(2L)
        );

        given(mentionRepository.findAllByOrderByCreatedAtDesc())
                .willReturn(mentions);

        final List<MentionDto.MentionResponse> mentionResponses = mentionService.readAllMentions();

        then(mentionResponses)
                .hasSize(2)
                .extracting("id")
                .contains(1L, 2L);
    }

    @Test
    void updateMention_ValidInput_NoException() throws Exception {
        final Mention mention = Mention.builder().id(1L).name("name").content("content").build();
        given(mentionRepository.findById(anyLong())).willReturn(Optional.of(mention));

        final String updatedName = "updated name";
        final String updatedContent = "updated content";

        final MentionDto.MentionRequest mentionRequest = MentionDto.MentionRequest.builder()
                .name(updatedName)
                .content(updatedContent)
                .build();

        try{
            mentionService.updateMention(1L, mentionRequest);
        }catch(Exception e){
            throw new Exception("Test Failed: " + e.getMessage());
        }

        then(mention)
                .hasFieldOrPropertyWithValue("name", updatedName)
                .hasFieldOrPropertyWithValue("content", updatedContent);
    }

    @Test
    void updateMention_NonExistentMentionId_MentionNotFoundException(){
        given(mentionRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        thenThrownBy(() -> mentionService.updateMention(1L, MentionDto.MentionRequest.builder().build()))
                .isExactlyInstanceOf(MentionNotFoundException.class);
    }

    @Test
    void deleteMention_ValidInput_NoException(){
        given(mentionRepository.findById(anyLong()))
                .willReturn(Optional.of(Mention.builder().build()));

        mentionService.deleteMention(1L);
    }

    @Test
    void deleteMention_NonExistentMentionId_MentionNotFoundException(){
        given(mentionRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        thenThrownBy(() -> mentionService.deleteMention(1L))
                .isExactlyInstanceOf(MentionNotFoundException.class);
    }
}