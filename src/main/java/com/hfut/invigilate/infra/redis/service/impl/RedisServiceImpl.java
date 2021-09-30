package com.hfut.invigilate.infra.redis.service.impl;

import com.hfut.invigilate.infra.redis.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    RedisTemplate redisTemplate;

    @Override
    public Set<Integer> getUnRead(String workId){
        return redisTemplate.opsForSet().members(workId + ":un_read");
    }

    @Override
    public void readReport(String workId, Long reportCode){
        redisTemplate.opsForSet().remove(workId+":un_read",reportCode);
    }

    @Override
    public void reportToTeacher(String workerId, Long reportCode){
        redisTemplate.opsForSet().add(workerId+":un_read",reportCode);
    }

    @Override
    public void reportToAdmin(Long reportCode){
        redisTemplate.opsForSet().add("0:un_read",reportCode);
    }

}
