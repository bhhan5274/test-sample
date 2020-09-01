package com.bhhan.guestbook.api.mention.web;

import com.bhhan.guestbook.api.mention.service.MentionService;
import com.bhhan.guestbook.api.mention.service.dto.MentionDto;
import com.bhhan.guestbook.api.mention.service.exception.MentionNotFoundException;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Created by hbh5274@gmail.com on 2020-09-01
 * Github : http://github.com/bhhan5274
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentions")
public class MentionController {
    private final MentionService mentionService;

    @Getter
    @Setter
    @NoArgsConstructor
    private static class Error {
        private int status;
        private String error;
        private String message;
        private String timestamp;

        @Builder
        public Error(int status, String error, String message, String timestamp){
            this.status = status;
            this.error = error;
            this.message = message;
            this.timestamp = timestamp;
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long create(@RequestBody @Valid MentionDto.MentionRequest mentionRequest){
        return mentionService.createMention(mentionRequest);
    }

    @GetMapping("/{id}")
    public MentionDto.MentionResponse read(@PathVariable Long id){
        return mentionService.readMention(id);
    }

    @GetMapping
    public List<MentionDto.MentionResponse> readAllMentions(){
        return mentionService.readAllMentions();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody @Valid MentionDto.MentionRequest mentionRequest){
        mentionService.updateMention(id, mentionRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        mentionService.deleteMention(id);
    }

    @ExceptionHandler(MentionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleMentionNotFoundException(MentionNotFoundException e){
        return Error.builder()
                .timestamp(Timestamp.valueOf(LocalDateTime.now()).toString())
                .status(HttpStatus.NOT_FOUND.value())
                .error(e.toString())
                .message(Objects.nonNull(e.getMessage()) ? e.getMessage() : "Not found Mention Object!")
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        return Error.builder()
                .timestamp(Timestamp.valueOf(LocalDateTime.now()).toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(e.toString())
                .message(e.getMessage())
                .build();
    }
}
