package com.springuni.metrics.engagement.google;

import static java.time.Instant.ofEpochMilli;
import static java.time.ZoneOffset.UTC;

import com.springuni.metrics.core.model.AbstractRecord;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class GooglePlusActivity extends AbstractRecord {

    private final String postId;
    private final String postUrl;
    private final String title;
    private final int replies;
    private final int plusoners;
    private final int resharers;
    private final String attachmentUrl;
    private final String attachmentType;
    private final LocalDateTime published;
    private final LocalDateTime updated;

    public GooglePlusActivity(
            long id, LocalDate effDate,
            String postId, String postUrl, String title,
            int replies, int plusoners, int resharers,
            String attachmentUrl, String attachmentType,
            LocalDateTime published, LocalDateTime updated) {

        super(id, effDate);

        this.postId = postId;
        this.postUrl = postUrl;
        this.title = title;
        this.replies = replies;
        this.plusoners = plusoners;
        this.resharers = resharers;
        this.attachmentUrl = attachmentUrl;
        this.attachmentType = attachmentType;
        this.published = published;
        this.updated = updated;
    }

    public static GooglePlusActivity of(
            long id, LocalDate effDate, String postId, String postUrl, String title, int replies,
            int plusoners, int resharers,
            String attachmentUrl, String attachmentType,
            Long published, Long updated) {

        LocalDateTime publishedDateTime = Optional.ofNullable(published)
                .map(Instant::ofEpochMilli)
                .map(it -> it.atOffset(UTC))
                .map(OffsetDateTime::toLocalDateTime)
                .orElse(null);

        LocalDateTime updatedDateTime = Optional.ofNullable(updated)
                .map(Instant::ofEpochMilli)
                .map(it -> it.atOffset(UTC))
                .map(OffsetDateTime::toLocalDateTime)
                .orElse(null);

        return new GooglePlusActivity(id, effDate,
                postId, postUrl, title, replies, plusoners, resharers, attachmentUrl,
                attachmentType, publishedDateTime, updatedDateTime);
    }

    private static LocalDateTime millsToLocalDateTime(long millis) {
        Instant instant = ofEpochMilli(millis);
        return instant.atOffset(UTC).toLocalDateTime();
    }

}
