package com.springuni.metrics.engagement.google;

import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;

public class GooglePlusActivityItemReader extends
        AbstractItemCountingItemStreamItemReader<Activity> {

    private static final String NO_NEXT_PAGE_TOKEN = "no-next-page";

    private final Plus plus;
    private final Queue<Activity> activityQueue = new LinkedList<>();

    private String nextPageToken;

    public GooglePlusActivityItemReader() {
        setName(getClass().getSimpleName());

        GoogleClientRequestInitializer clientRequestInitializer =
                new CommonGoogleClientRequestInitializer("AIzaSyCGwuyZixI-l-1G8stoUWhLtrUDAVOs4CM");

        plus = new Plus.Builder(
                new NetHttpTransport(), JacksonFactory.getDefaultInstance(), null)
                .setGoogleClientRequestInitializer(clientRequestInitializer)
                .setApplicationName(getClass().getName())
                .build();
    }

    @Override
    protected Activity doRead() throws Exception {
        Activity activity = activityQueue.poll();
        if (activity != null) {
            return activity;
        }

        fetchIfNeeded();

        return activityQueue.poll();
    }

    @Override
    protected void doOpen() throws Exception {
        fetchIfNeeded();
    }

    @Override
    protected void doClose() throws Exception {
        activityQueue.clear();
        nextPageToken = null;
    }

    private void fetchIfNeeded() throws IOException {
        if (NO_NEXT_PAGE_TOKEN.equals(nextPageToken) || !activityQueue.isEmpty()) {
            return;
        }

        ActivityFeed activityFeed = plus.activities()
                .list("+springframework", "public")
                .setPageToken(nextPageToken)
                .execute();

        activityQueue.addAll(activityFeed.getItems());

        nextPageToken = Optional.ofNullable(activityFeed.getNextPageToken())
                .orElse(NO_NEXT_PAGE_TOKEN);
    }

}
