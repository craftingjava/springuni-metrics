package com.springuni.metrics.follower;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class SocialFollowerCount {

  private final SocialNetwork socialNetwork;
  private final int followers;

}
