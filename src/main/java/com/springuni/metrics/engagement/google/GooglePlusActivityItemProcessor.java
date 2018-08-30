package com.springuni.metrics.engagement.google;

import static com.springuni.metrics.core.batch.JobParameterKeys.EFF_DATE;
import static java.time.ZoneOffset.UTC;

import com.google.api.client.util.DateTime;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.Activity.PlusObject;
import com.google.api.services.plus.model.Activity.PlusObject.Attachments;
import com.google.api.services.plus.model.Activity.PlusObject.Plusoners;
import com.google.api.services.plus.model.Activity.PlusObject.Replies;
import com.google.api.services.plus.model.Activity.PlusObject.Resharers;
import com.springuni.metrics.core.util.IdentityGenerator;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.Assert;

public class GooglePlusActivityItemProcessor
    extends StepExecutionListenerSupport implements ItemProcessor<Activity, GooglePlusActivity> {

  private LocalDate effDate;

  private static final Logger log = LoggerFactory.getLogger(GooglePlusActivityItemProcessor.class);

  @Override
  public void beforeStep(StepExecution stepExecution) {
    effDate = Optional.ofNullable(stepExecution.getJobParameters().getDate(EFF_DATE.name()))
        .map(Date::toInstant)
        .map(it -> it.atOffset(UTC))
        .map(OffsetDateTime::toLocalDate)
        .orElse(null);

    super.beforeStep(stepExecution);
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    effDate = null;

    // Return null to leave the old value unchanged.
    return null;
  }

  @Override
  public GooglePlusActivity process(final Activity activity) {
    Assert.notNull(effDate, "Effective date is null");

    Optional<PlusObject> optionalPlusObject = Optional.ofNullable(activity.getObject());

    int replies = optionalPlusObject
        .map(PlusObject::getReplies)
        .map(Replies::getTotalItems)
        .map(Long::intValue)
        .orElse(0);

    int plusoners = optionalPlusObject
        .map(PlusObject::getPlusoners)
        .map(Plusoners::getTotalItems)
        .map(Long::intValue)
        .orElse(0);

    int resharers = optionalPlusObject
        .map(PlusObject::getResharers)
        .map(Resharers::getTotalItems)
        .map(Long::intValue)
        .orElse(0);

    Optional<Attachments> optionalAttachments = optionalPlusObject
        .map(PlusObject::getAttachments)
        .filter(it -> !it.isEmpty())
        .map(it -> it.get(0));

    String attachmentType = optionalAttachments
        .map(Attachments::getObjectType).orElse(null);

    String attachmentUrl = optionalAttachments
        .map(Attachments::getUrl).orElse(null);

    Long published = Optional.ofNullable(activity.getPublished())
        .map(DateTime::getValue)
        .orElse(null);

    Long updated = Optional.ofNullable(activity.getUpdated())
        .map(DateTime::getValue)
        .orElse(null);

    long id = IdentityGenerator.getInstance().generate();

    return GooglePlusActivity.of(
        id, effDate, activity.getId(), activity.getUrl(), activity.getTitle(), replies, plusoners,
        resharers, attachmentUrl, attachmentType, published, updated
    );
  }

}
