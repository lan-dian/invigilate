package com.hfut.invigilate.infra.redis.service;

import java.util.List;
import java.util.Set;

public interface RedisService {
    Set<Integer> getUnRead(String workId);

    void readReport(String workId, Long reportCode);

    void reportToTeacher(String workerId, Long reportCode);

    void reportToAdmin(Long reportCode);
}
