package com.bhhan.guestbook.api.mention.service.dto;

import com.bhhan.guestbook.api.mention.domain.Mention;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * Created by hbh5274@gmail.com on 2020-09-01
 * Github : http://github.com/bhhan5274
 */
public class MentionDto {
    @Getter
    @Setter
    public static class MentionRequest {
        @NotEmpty
        private String name;
        @NotEmpty
        private String content;

        @Builder
        public MentionRequest(String name, String content){
            this.name = name;
            this.content = content;
        }
    }

    @Getter
    @Setter
    public static class MentionResponse {
        private Long id;
        private String name;
        private String content;
        private LocalDateTime createdAt;

        private MentionResponse(Mention mention){
            this.id = mention.getId();
            this.name = mention.getName();
            this.content = mention.getContent();
            this.createdAt = mention.getCreatedAt();
        }

        public static MentionResponse of(Mention mention){
            return new MentionResponse(mention);
        }
    }
}
