package com.bhhan.guestbook.api.mention.web;

import com.bhhan.guestbook.api.mention.service.MentionService;
import com.bhhan.guestbook.api.mention.service.dto.MentionDto;
import com.bhhan.guestbook.api.mention.service.exception.MentionNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.bhhan.guestbook.api.mention.domain.MentionTest.getMentionResponseFixture;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by hbh5274@gmail.com on 2020-09-01
 * Github : http://github.com/bhhan5274
 */

@WebMvcTest
class MentionControllerTest {
    private static final String BASE_URL = "/api/v1/mentions";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MentionService mentionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void get_ValidInput_MentionResponse() throws Exception {
        final MentionDto.MentionResponse mentionResponse = getMentionResponseFixture(1L);

        given(mentionService.readMention(anyLong())).willReturn(mentionResponse);

        mockMvc.perform(get(BASE_URL + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("name"))
                .andExpect(jsonPath("content").value("content"))
                .andExpect(jsonPath("createdAt").isNotEmpty());
    }

    @Test
    void get_NonExistentId_ApiError() throws Exception {
        given(mentionService.readMention(anyLong()))
                .willThrow(new MentionNotFoundException(1L));

        mockMvc.perform(get(BASE_URL + "/{id}", 1))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("status").isNumber())
                .andExpect(jsonPath("error").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty());
    }

    @Test
    void getList_ValidInput_MentionResponse() throws Exception {
        final List<MentionDto.MentionResponse> mentionResponses = Arrays.asList(
                getMentionResponseFixture(1L),
                getMentionResponseFixture(2L)
        );

        given(mentionService.readAllMentions())
                .willReturn(mentionResponses);

        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void post_ValidInput_MentionResponse() throws Exception {
        given(mentionService.createMention(any(MentionDto.MentionRequest.class)))
                .willReturn(1L);

        String requestBody = objectMapper.writeValueAsString(MentionDto.MentionRequest
                .builder()
                .name("name")
                .content("content")
                .build());

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    void post_EmptyFields_ApiError() throws Exception {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        final String requestBody = objectMapper.writeValueAsString(MentionDto.MentionRequest.builder());

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("status").isNumber())
                .andExpect(jsonPath("error").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty());

    }

    @Test
    void put_ValidInput_MentionResponse() throws Exception {
        String requestBody = objectMapper.writeValueAsString(MentionDto.MentionRequest
                .builder()
                .name("name")
                .content("content")
                .build());

        mockMvc.perform(put(BASE_URL + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void put_EmptyFields_ApiError() throws Exception {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        final String requestBody = objectMapper.writeValueAsString(MentionDto.MentionRequest.builder());

        mockMvc.perform(put(BASE_URL + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").isNotEmpty())
                .andExpect(jsonPath("status").isNumber())
                .andExpect(jsonPath("error").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty());
    }

    @Test
    void delete_ValidInput_MentionResponse() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", 1))
                .andExpect(status().isOk());
    }
}