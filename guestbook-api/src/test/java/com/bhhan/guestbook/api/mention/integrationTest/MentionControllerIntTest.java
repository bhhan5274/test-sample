package com.bhhan.guestbook.api.mention.integrationTest;

import com.bhhan.guestbook.api.mention.service.dto.MentionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by hbh5274@gmail.com on 2020-09-01
 * Github : http://github.com/bhhan5274
 */

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Sql("classpath:data/mentions.sql")
class MentionControllerIntTest {
    private final String BASE_URL = "/api/v1/mentions";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getMention_ValidInput_ValidOutput() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.content").value("content1"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void getAllMentions_ValidInput_ValidOutput() throws Exception {
        this.mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void postMention_ValidInput_GetIdOfGeneratedMention() throws Exception {
        final MentionDto.MentionRequest mentionRequest = MentionDto.MentionRequest.builder()
                .name("arbitary name")
                .content("arbitary content")
                .build();

        this.mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mentionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void putMention_ValidInput_StatusOkAndGetTheModifiedOne() throws Exception {
        final String modifiedName = "modified name";
        final String modifiedContent = "modified content";

        final MentionDto.MentionRequest mentionRequest = MentionDto.MentionRequest.builder()
                .name(modifiedName)
                .content(modifiedContent)
                .build();

        this.mockMvc.perform(put(BASE_URL + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mentionRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        this.mockMvc.perform(get(BASE_URL + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(modifiedName))
                .andExpect(jsonPath("$.content").value(modifiedContent))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void deleteMentionAndGetTheDeleteOne_ValidInput_StatusOkAndFailToGetDeletedOne() throws Exception {
        this.mockMvc.perform(delete(BASE_URL + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        this.mockMvc.perform(get(BASE_URL + "/{id}", 1))
                .andExpect(status().isNotFound());
    }
}
