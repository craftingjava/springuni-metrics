package com.springuni.metrics;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.support.MessageBuilder;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@RequiredArgsConstructor
public class TwitterFollowersHandlerImpl
    implements EnvironmentAware, InitializingBean, TwitterFollowersHandler {

  private Environment environment;
  private String screenName;
  private Twitter twitter;

  @Override
  public void afterPropertiesSet() {
    screenName = environment.getProperty("twitter.screen-name");
    twitter = createTwitter();
  }

  @Override
  public Integer handle(LocalDate payload, Map<String, Object> headers) {
    try {
      return twitter.showUser(screenName).getFollowersCount();
    } catch (TwitterException e) {
      Message<LocalDate> failedMessage = MessageBuilder.withPayload(payload)
          .copyHeaders(headers)
          .build();

      throw new MessageHandlingException(failedMessage, e.getErrorMessage(), e);
    }
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  private Twitter createTwitter() {
    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

    Optional.ofNullable(environment.getProperty("twitter.consumer-key"))
        .ifPresent(configurationBuilder::setOAuthConsumerKey);
    Optional.ofNullable(environment.getProperty("twitter.consumer-secret"))
        .ifPresent(configurationBuilder::setOAuthConsumerSecret);
    Optional.ofNullable(environment.getProperty("twitter.access-token"))
        .ifPresent(configurationBuilder::setOAuthAccessToken);
    Optional.ofNullable(environment.getProperty("twitter.access-token-secret"))
        .ifPresent(configurationBuilder::setOAuthAccessTokenSecret);

    return new TwitterFactory(configurationBuilder.build()).getInstance();
  }

}
