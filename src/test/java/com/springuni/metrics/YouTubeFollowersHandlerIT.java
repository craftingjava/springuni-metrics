package com.springuni.metrics;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import org.junit.Before;
import org.junit.Test;

public class YouTubeFollowersHandlerIT {

  private YouTubeFollowersHandler youTubeFollowersHandler;

  @Before
  public void setUp() {
    GoogleClientRequestInitializer clientRequestInitializer =
        new CommonGoogleClientRequestInitializer("");

    YouTube youTube = new YouTube.Builder(
        new NetHttpTransport(), JacksonFactory.getDefaultInstance(), null)
        .setGoogleClientRequestInitializer(clientRequestInitializer)
        .setApplicationName(getClass().getName())
        .build();

    youTubeFollowersHandler = new YouTubeFollowersHandlerImpl(youTube);
  }

  @Test
  public void handle() {
    int followers = youTubeFollowersHandler.handle(now(), emptyMap());
    assertThat(followers, greaterThanOrEqualTo(0));
  }

}
