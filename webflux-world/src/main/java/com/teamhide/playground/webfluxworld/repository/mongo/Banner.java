package com.teamhide.playground.webfluxworld.repository.mongo;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "banner")
@Getter
public class Banner {
    @Id
    private String id;
    private String imageUrl;
    private String title;
    private String subTitle;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Banner of(final String imageUrl, final String title, final String subTitle) {
        final Banner banner = new Banner();
        banner.imageUrl = imageUrl;
        banner.title = title;
        banner.subTitle = subTitle;
        return banner;
    }
}
