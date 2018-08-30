package com.springuni.metrics;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;

public class GooglePlusFollowerCountFetcherIT {

  private SocialFollowerCountFetcher socialFollowerCountFetcher;

  @Before
  public void setUp() {
    GoogleClientRequestInitializer clientRequestInitializer =
        new CommonGoogleClientRequestInitializer("");

    Plus plus = new Plus.Builder(
        new NetHttpTransport(), JacksonFactory.getDefaultInstance(), null)
        .setGoogleClientRequestInitializer(clientRequestInitializer)
        .setApplicationName(getClass().getName())
        .build();

    socialFollowerCountFetcher = new GooglePlusFollowerCountFetcherImpl(plus);
  }

  @Test
  public void handle() {
    Message<SocialFollowerCount> followerMessage = socialFollowerCountFetcher.handle(now(), emptyMap());
    assertThat(followerMessage.getPayload().getFollowers(), greaterThan(0));
  }

}
