package com.chat.app.firebasechat;

/**
 * @author greg
 * @since 6/21/13
 */
public class Chat {

    private String message;
    private String author;
    private String val;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Chat() {
    }

    Chat(String message, String author, String val) {
        this.message = message;
        this.author = author;
        this.val = val;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
    public String getVal() {
        return val;
    }
}
