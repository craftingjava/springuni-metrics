package com.springuni.metrics;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;

import com.springuni.metrics.TwitterFollowersHandlerIT.TestConfig;
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
public class TwitterFollowersHandlerIT {

  @Autowired
  private Environment environment;

  @Autowired
  private SocialFollowersHandler twitterFollowersHandler;

  @Test
  public void handle() {
    assumeTrue("consumer-key", environment.containsProperty("twitter.consumer-key"));
    assumeTrue("consumer-secret", environment.containsProperty("twitter.consumer-secret"));
    assumeTrue("access-token", environment.containsProperty("twitter.access-token"));
    assumeTrue("access-token-secret", environment.containsProperty("twitter.access-token-secret"));

    Message<SocialFollowerCount> followerMessage = twitterFollowersHandler.handle(now(), emptyMap());
    assertThat(followerMessage.getPayload().getFollowers(), greaterThan(0));
  }

  @Configuration
  static class TestConfig {

    @Bean
    SocialFollowersHandler twitterFollowersHandler() {
      return new TwitterFollowersHandlerImpl();
    }

  }

}
