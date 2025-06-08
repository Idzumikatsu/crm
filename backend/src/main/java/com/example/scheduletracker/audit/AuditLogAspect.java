package com.example.scheduletracker.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditLogAspect {
  private final AuditLogRepository repo;
  private final ObjectMapper mapper;

  public AuditLogAspect(AuditLogRepository repo, ObjectMapper mapper) {
    this.repo = repo;
    this.mapper = mapper;
  }

  @Pointcut("@annotation(track)")
  public void callAt(Track track) {}

  @AfterReturning(value = "callAt(track)", returning = "result")
  public void log(JoinPoint jp, Track track, Object result) throws Throwable {
    Object arg = jp.getArgs().length > 0 ? jp.getArgs()[0] : null;
    String oldJson = arg != null ? mapper.writeValueAsString(arg) : null;
    String newJson = result != null ? mapper.writeValueAsString(result) : null;
    AuditLog log =
        new AuditLog(track.entity(), null, jp.getSignature().getName(), oldJson, newJson, null);
    repo.save(log);
  }
}
