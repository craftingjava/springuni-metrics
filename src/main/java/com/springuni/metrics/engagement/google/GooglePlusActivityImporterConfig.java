package com.springuni.metrics.engagement.google;

import com.google.api.services.plus.model.Activity;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class GooglePlusActivityImporterConfig {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  // tag::readerwriterprocessor[]
  @Bean
  public ItemReader<Activity> reader() {
    return new GooglePlusActivityItemReader();
  }

  @Bean
  public GooglePlusActivityItemProcessor processor() {
    return new GooglePlusActivityItemProcessor();
  }

  @Bean
  public JdbcBatchItemWriter<GooglePlusActivity> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<GooglePlusActivity>()
        .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
        .sql("INSERT INTO google_activity "
            + "(id, eff_date, post_id, post_url, title, replies, plusoners, resharers, attachment_url, attachment_type, published, updated) VALUES "
            + "(:id, :effDate, :postId, :postUrl, :title, :replies, :plusoners, :resharers, :attachmentUrl, :attachmentType, :published, :updated)")
        .dataSource(dataSource)
        .build();
  }
  // end::readerwriterprocessor[]

  // tag::jobstep[]
  @Bean
  public Job job(Step step) {
    return jobBuilderFactory.get("importUserJob")
        .incrementer(new RunIdIncrementer())
        //.listener(listener)
        .flow(step)
        .end()
        .build();
  }

  @Bean
  public Step step(JdbcBatchItemWriter<GooglePlusActivity> writer) {
    return stepBuilderFactory.get("step1")
        .<Activity, GooglePlusActivity>chunk(20)
        .reader(reader())
        .processor(processor())
        .writer(writer)
        .build();
  }

}
