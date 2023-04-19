package com.sr29_2021.Model;

import java.time.LocalDateTime;

public class News {
    private Integer id;
    private String name;
    private String content;
    private LocalDateTime dateTime;

    public News(Integer id, String name, String content, LocalDateTime dateTime) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.dateTime = dateTime;
    }

    public News() {
        this.dateTime = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
