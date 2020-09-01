package com.bhhan.guestbook.api.mention.service;

import com.bhhan.guestbook.api.mention.domain.Mention;
import com.bhhan.guestbook.api.mention.domain.MentionRepository;
import com.bhhan.guestbook.api.mention.service.dto.MentionDto;
import com.bhhan.guestbook.api.mention.service.exception.MentionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hbh5274@gmail.com on 2020-09-01
 * Github : http://github.com/bhhan5274
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MentionService {
    private final MentionRepository mentionRepository;

    public Long createMention(MentionDto.MentionRequest mentionRequest){
        final Mention createdMention = mentionRepository.save(Mention.builder()
                .name(mentionRequest.getName())
                .content(mentionRequest.getContent())
                .build());

        return createdMention.getId();
    }

    public MentionDto.MentionResponse readMention(Long id){
        return MentionDto.MentionResponse.of(findByMention(id));
    }

    public List<MentionDto.MentionResponse> readAllMentions(){
        final ArrayList<MentionDto.MentionResponse> mentionResponses = new ArrayList<>();

        mentionRepository.findAllByOrderByCreatedAtDesc()
                .forEach(mention -> mentionResponses.add(MentionDto.MentionResponse.of(mention)));

        return mentionResponses;
    }

    public void updateMention(Long id, MentionDto.MentionRequest mentionRequest){
        final Mention mention = findByMention(id);
        mention.update(mentionRequest.getName(), mentionRequest.getContent());
    }

    public void deleteMention(Long id){
        mentionRepository.delete(findByMention(id));
    }

    private Mention findByMention(Long id) {
        return mentionRepository.findById(id)
                .orElseThrow(() -> new MentionNotFoundException(id));
    }
}
