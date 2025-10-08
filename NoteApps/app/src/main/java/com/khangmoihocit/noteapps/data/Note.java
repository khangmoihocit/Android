package com.khangmoihocit.noteapps.data;

public class Note {
    public long id;
    public String title;
    public String content;
    public long createdAt;  // Thời gian tạo (dưới dạng apoch millis)
    public long updatedAt;  // Thời gian cập nhật lần cuối (dưới dạng apoch millis)
    public Long remindAt;   // Thời gian nhắc nhở, có thể là null
}