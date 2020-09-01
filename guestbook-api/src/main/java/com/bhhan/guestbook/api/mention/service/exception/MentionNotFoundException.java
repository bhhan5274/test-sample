package com.bhhan.guestbook.api.mention.service.exception;

/**
 * Created by hbh5274@gmail.com on 2020-09-01
 * Github : http://github.com/bhhan5274
 */
public class MentionNotFoundException extends RuntimeException{
    private Long id;

    public MentionNotFoundException(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
