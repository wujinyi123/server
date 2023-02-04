package com.system.xiumei.aop;

import com.alibaba.fastjson.JSONObject;
import com.system.base.domain.TaskResult;
import com.system.base.util.ThreadPoolUtil;
import com.system.xiumei.mapper.IDemoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RestAop {
    @Autowired
    private IDemoMapper demoMapper;

    @Pointcut("@annotation(com.system.base.inter.BackgroundTask)")
    private static void restAop(){}

    @Around("restAop()")
    public Object invoke(ProceedingJoinPoint joinPoint) throws Exception {
        TaskResult taskResult = new TaskResult();
        Runnable task = ()->{
            try {
                taskResult.setResult(joinPoint.proceed());
                taskResult.setIsAlive(false);
                Thread.sleep(2000);
                if (StringUtils.isNotEmpty(taskResult.getTaskCode())) {
                    demoMapper.updateDemo(taskResult.getTaskCode(), JSONObject.toJSONString(taskResult.getResult()));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        };
        ThreadPoolUtil.getInstance().startTask(task);
        for (int i=0;i<5;i++) {
            Thread.sleep(1000);
            if (!taskResult.getIsAlive()) {
                return taskResult.getResult();
            }
        }
        taskResult.setTaskCode(System.currentTimeMillis()+"");
        demoMapper.addDemo(taskResult.getTaskCode());
        return taskResult;
    }
}
