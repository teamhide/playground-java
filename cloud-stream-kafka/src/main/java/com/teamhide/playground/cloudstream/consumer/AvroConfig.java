package com.teamhide.playground.cloudstream.consumer;

import org.springframework.cloud.stream.schema.registry.avro.AvroSchemaMessageConverter;
import org.springframework.cloud.stream.schema.registry.avro.AvroSchemaServiceManager;
import org.springframework.cloud.stream.schema.registry.avro.AvroSchemaServiceManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AvroConfig {
    @Bean
    public AvroSchemaServiceManager avroSchemaServiceManager() {
        return new AvroSchemaServiceManagerImpl();
    }

    @Bean
    public AvroSchemaMessageConverter avroSchemaMessageConverter(final AvroSchemaServiceManager avroSchemaServiceManager) {
        return new AvroSchemaMessageConverter(avroSchemaServiceManager);
    }
}
