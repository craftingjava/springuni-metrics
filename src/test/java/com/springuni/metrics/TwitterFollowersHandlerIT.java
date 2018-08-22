package com.springuni.metrics;

import static java.time.LocalDate.now;
import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assume.assumeTrue;

import com.springuni.metrics.TwitterFollowersHandlerIT.TestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class TwitterFollowersHandlerIT {

  @Autowired
  private Environment environment;

  @Autowired
  private TwitterFollowersHandler twitterFollowersHandler;

  @Test
  public void handle() {
    assumeTrue("consumer-key", environment.containsProperty("twitter.consumer-key"));
    assumeTrue("consumer-secret", environment.containsProperty("twitter.consumer-secret"));
    assumeTrue("access-token", environment.containsProperty("twitter.access-token"));
    assumeTrue("access-token-secret", environment.containsProperty("twitter.access-token-secret"));

    int followers = twitterFollowersHandler.handle(now(), emptyMap());

    Assert.assertThat(followers, greaterThan(0));
  }

  @Configuration
  static class TestConfig {

    @Bean
    TwitterFollowersHandler twitterFollowersHandler() {
      return new TwitterFollowersHandlerImpl();
    }

  }

}
