package com.springuni.metrics;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.springuni.metrics.follower.SocialFollowerCount;
import com.springuni.metrics.follower.SocialFollowerCountFetcher;
import com.springuni.metrics.follower.youtube.YouTubeFollowerCountFetcherImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;

public class YouTubeFollowerCountFetcherIT {

  private SocialFollowerCountFetcher socialFollowerCountFetcher;

  @Before
  public void setUp() {
    GoogleClientRequestInitializer clientRequestInitializer =
        new CommonGoogleClientRequestInitializer("");

    YouTube youTube = new YouTube.Builder(
        new NetHttpTransport(), JacksonFactory.getDefaultInstance(), null)
        .setGoogleClientRequestInitializer(clientRequestInitializer)
        .setApplicationName(getClass().getName())
        .build();

    socialFollowerCountFetcher = new YouTubeFollowerCountFetcherImpl(youTube);
  }

  @Test
  public void handle() {
    Message<SocialFollowerCount> followerMessage = socialFollowerCountFetcher.handle(now(), emptyMap());
    assertThat(followerMessage.getPayload().getFollowers(), greaterThan(0));
  }

}
