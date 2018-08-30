package com.springuni.metrics.follower;

import static org.springframework.integration.IntegrationMessageHeaderAccessor.CORRELATION_ID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@Slf4j
@Configuration
public class SocialFollowerCountFlow {

  @Bean
  MessageChannel triggerChannel() {
    return new DirectChannel();
  }

  @Bean
  MessageChannel resultChannel() {
    return new DirectChannel();
  }

  @Bean
  public IntegrationFlow triggerFlow(MessageChannel triggerChannel) {
    return IntegrationFlows
        .from(
            () -> MessageBuilder.withPayload(1)
                .setHeader(CORRELATION_ID, System.currentTimeMillis()).build(),
            c -> c.poller(Pollers.fixedRate(1000).maxMessagesPerPoll(1))
        )
        .channel(triggerChannel)
        .get();
  }

  @Bean
  public IntegrationFlow scatterGatherFlow(
      MessageChannel triggerChannel, MessageChannel resultChannel) {

    return IntegrationFlows
        .from(triggerChannel)
        .scatterGather(scatterer -> scatterer
                .recipientFlow(m -> true, subFlow -> subFlow.<Integer>handle((p, h) -> p + 1))
                .recipientFlow(m -> true, subFlow -> subFlow.<Integer>handle((p, h) -> p + 2)),
            gatherer -> gatherer
                .releaseStrategy(group -> group.size() == 2),
            scatterGatherSpec -> scatterGatherSpec.gatherTimeout(30_000))
        .channel(resultChannel)
        .get();
  }

  @Bean
  public IntegrationFlow resultFlow(MessageChannel resultChannel) {
    return IntegrationFlows.from(resultChannel)
        .log(Level.ERROR, message -> "RECEIVED: " + message.getPayload())
        .handle(m -> {
        })
        .get();
  }

}
