package com.springuni.metrics;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.*;

import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.youtube.YouTube;
import org.junit.Before;
import org.junit.Test;

public class GooglePlusFollowersHandlerIT {

  private GooglePlusFollowersHandler googlePlusFollowersHandler;

  @Before
  public void setUp() {
    GoogleClientRequestInitializer clientRequestInitializer =
        new CommonGoogleClientRequestInitializer("");

    Plus plus = new Plus.Builder(
        new NetHttpTransport(), JacksonFactory.getDefaultInstance(), null)
        .setGoogleClientRequestInitializer(clientRequestInitializer)
        .setApplicationName(getClass().getName())
        .build();

    googlePlusFollowersHandler = new GooglePlusFollowersHandlerImpl(plus);
  }

  @Test
  public void handle() {
    int followers = googlePlusFollowersHandler.handle(now(), emptyMap());
    assertThat(followers, greaterThanOrEqualTo(0));
  }

}
