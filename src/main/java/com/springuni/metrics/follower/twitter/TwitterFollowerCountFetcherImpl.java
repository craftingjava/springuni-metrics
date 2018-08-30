package com.springuni.metrics.follower.twitter;

import static com.springuni.metrics.follower.SocialNetwork.TWITTER;

import com.springuni.metrics.follower.AbstractSocialFollowerCountFetcher;
import java.util.Optional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterFollowerCountFetcherImpl
    extends AbstractSocialFollowerCountFetcher implements EnvironmentAware, InitializingBean {

  private Environment environment;
  private String screenName;
  private Twitter twitter;

  public TwitterFollowerCountFetcherImpl() {
    super(TWITTER);
  }

  @Override
  public void afterPropertiesSet() {
    screenName = environment.getProperty("twitter.screen-name");
    twitter = createTwitter();
  }

  @Override
  protected int fetchFollowers() {
    try {
      return twitter.showUser(screenName).getFollowersCount();
    } catch (TwitterException e) {
      throw new RuntimeException(e);
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
