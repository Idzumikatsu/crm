package com.example.scheduletracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
public class ScheduleTrackerApplication {
  public static void main(String[] args) {
    SpringApplication.run(ScheduleTrackerApplication.class, args);
  }
}
