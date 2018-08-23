package com.springuni.metrics;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import org.junit.Before;
import org.junit.Test;

public class FacebookFollowersHandlerIT {

  private FacebookFollowersHandler facebookFollowersHandler;

  @Before
  public void setUp() throws Exception {
    FacebookClient facebookClient = new DefaultFacebookClient("", "", Version.VERSION_3_1);
    facebookFollowersHandler = new FacebookFollowersHandlerImpl(facebookClient);
  }

  @Test
  public void handle() {
    int followers = facebookFollowersHandler.handle(now(), emptyMap());
    assertThat(followers, greaterThan(0));
  }

}
