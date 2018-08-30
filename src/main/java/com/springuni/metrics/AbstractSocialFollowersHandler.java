package com.springuni.metrics;

import java.time.LocalDate;
import java.util.Map;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public abstract class AbstractSocialFollowersHandler implements SocialFollowersHandler {

  private final SocialNetwork socialNetwork;

  public AbstractSocialFollowersHandler(SocialNetwork socialNetwork) {
    this.socialNetwork = socialNetwork;
  }

  @Override
  public Message<SocialFollowerCount> handle(LocalDate payload, Map<String, Object> headers) {
    int followers = fetchFollowers();
    SocialFollowerCount socialFollowerCount = SocialFollowerCount.of(socialNetwork, followers);
    return MessageBuilder.withPayload(socialFollowerCount).copyHeaders(headers).build();
  }

  protected abstract int fetchFollowers();

}
