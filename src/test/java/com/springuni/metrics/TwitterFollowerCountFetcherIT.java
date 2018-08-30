package com.springuni.metrics;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;

import com.springuni.metrics.TwitterFollowerCountFetcherIT.TestConfig;
import com.springuni.metrics.follower.SocialFollowerCount;
import com.springuni.metrics.follower.SocialFollowerCountFetcher;
import com.springuni.metrics.follower.twitter.TwitterFollowerCountFetcherImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TwitterFollowerCountFetcherIT {

  @Autowired
  private Environment environment;

  @Autowired
  private SocialFollowerCountFetcher socialFollowerCountFetcher;

  @Test
  public void handle() {
    assumeTrue("consumer-key", environment.containsProperty("twitter.consumer-key"));
    assumeTrue("consumer-secret", environment.containsProperty("twitter.consumer-secret"));
    assumeTrue("access-token", environment.containsProperty("twitter.access-token"));
    assumeTrue("access-token-secret", environment.containsProperty("twitter.access-token-secret"));

    Message<SocialFollowerCount> followerMessage = socialFollowerCountFetcher.handle(now(), emptyMap());
    assertThat(followerMessage.getPayload().getFollowers(), greaterThan(0));
  }

  @Configuration
  static class TestConfig {

    @Bean
    SocialFollowerCountFetcher twitterFollowersHandler() {
      return new TwitterFollowerCountFetcherImpl();
    }

  }

}
