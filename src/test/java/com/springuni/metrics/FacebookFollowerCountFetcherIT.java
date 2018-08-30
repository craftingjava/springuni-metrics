package com.springuni.metrics;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.springuni.metrics.follower.SocialFollowerCount;
import com.springuni.metrics.follower.SocialFollowerCountFetcher;
import com.springuni.metrics.follower.facebook.FacebookFollowerCountFetcherImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;

public class FacebookFollowerCountFetcherIT {

  private SocialFollowerCountFetcher socialFollowerCountFetcher;

  @Before
  public void setUp() {
    FacebookClient facebookClient = new DefaultFacebookClient("", "", Version.VERSION_3_1);
    socialFollowerCountFetcher = new FacebookFollowerCountFetcherImpl(facebookClient);
  }

  @Test
  public void handle() {
    Message<SocialFollowerCount> followerMessage = socialFollowerCountFetcher.handle(now(), emptyMap());
    assertThat(followerMessage.getPayload().getFollowers(), greaterThan(0));
  }

}
