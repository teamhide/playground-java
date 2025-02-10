package com.teamhide.playground.webfluxworld.common.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableR2dbcAuditing
@EnableTransactionManagement
@EnableR2dbcRepositories(basePackages = "com.teamhide.playground.webfluxworld.repository.rdb")
public class R2dbcConfig {

}
