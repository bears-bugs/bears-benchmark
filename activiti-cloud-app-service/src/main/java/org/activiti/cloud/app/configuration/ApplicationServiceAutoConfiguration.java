package org.activiti.cloud.app.configuration;

import org.activiti.cloud.app.rest.ApplicationsController;
import org.activiti.cloud.app.rest.DeploymentsController;
import org.activiti.cloud.app.services.ApplicationsService;
import org.activiti.cloud.app.services.DeploymentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@ConditionalOnProperty(name = "activiti.cloud.services.app-service.enabled", matchIfMissing = true)
@PropertySource(value = "classpath:app-service.properties",ignoreResourceNotFound = true)
@EnableDiscoveryClient
@EnableScheduling
public class ApplicationServiceAutoConfiguration {

    @Configuration
    @Import({ApplicationsController.class, DeploymentsController.class})
    public static class DefaultApplicationsServiceConfiguration implements SchedulingConfigurer {

        @Bean
        @ConditionalOnMissingBean(type = "ApplicationsService")
        ApplicationsService applicationsService() {
            return new ApplicationsService();
        }

        @Bean
        @ConditionalOnMissingBean(type = "DeploymentsService")
        DeploymentsService deploymentsService() {
            return new DeploymentsService();
        }

        @Bean
        @ConditionalOnMissingBean(type = "RestTemplate")
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Value("${activiti.cloud.app.service.refresh.rate:30000}")
        private int refreshRate;

        private static final Logger LOGGER = LoggerFactory.getLogger(DefaultApplicationsServiceConfiguration.class);

        @Bean(destroyMethod = "shutdown")
        public Executor taskExecutor() {
            return Executors.newScheduledThreadPool(100);
        }


        @Override
        public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
            taskRegistrar.setScheduler(taskExecutor());
            taskRegistrar.addTriggerTask(
                    new Runnable() {
                        @Override public void run() {
                            LOGGER.debug(">>> Refreshing Apps now: " + System.currentTimeMillis());
                            applicationsService().refresh();
                        }
                    },
                    new Trigger() {
                        @Override public Date nextExecutionTime(TriggerContext triggerContext) {
                            Calendar nextExecutionTime =  new GregorianCalendar();
                            Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
                            nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
                            nextExecutionTime.add(Calendar.MILLISECOND, refreshRate);
                            return nextExecutionTime.getTime();
                        }
                    }
            );
        }
    }
}
