package com.illegalaccess.delay.aop;

import com.illegalaccess.delay.toolkit.annotation.API;
import com.illegalaccess.delay.toolkit.dto.BaseResponse;
import com.illegalaccess.delay.toolkit.json.JsonTool;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 拦截对外暴露的接口，打印出参入参和执行耗时
 * @author Jimmy Li
 */
@Slf4j
@Aspect
@Component("delayPerfAop")
public class PerfAop {

    @Pointcut("@annotation(com.illegalaccess.delay.toolkit.annotation.API)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method targetMethod = methodSignature.getMethod();
        API api = targetMethod.getAnnotation(API.class);

        String apiName = pjp.getSignature().toString();
        Object[] params = pjp.getArgs();
        long startTimeMillis = System.currentTimeMillis();
        try {

            Object result = pjp.proceed();
            long endTimeMillis = System.currentTimeMillis();

            Object[] logParam = new Object[4];
            logParam[0] = apiName;
            logParam[1] = params.length == 0 ? "" : JsonTool.toJsonString(params);
            if (api.logResp()) {
                logParam[2] = JsonTool.toJsonString(result);
            } else {
                logParam[2] = "result not log";
            }

            logParam[3] = endTimeMillis - startTimeMillis;
            log.info("delay.{},方法参数:[{}],方法执行结果:[{}],耗时:[{}ms]", logParam);
            return result;
        } catch (Throwable throwable) {
            log.error("调用delay.{} fail with param:{}", apiName, JsonTool.toJsonString(params), throwable);
            return BaseResponse.fail("", "");
        }

    }
}
