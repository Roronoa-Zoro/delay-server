package com.illegalaccess.delay.core.web;

import com.illegalaccess.delay.client.dto.CancelMessageReq;
import com.illegalaccess.delay.core.DelayCoreProperties;
import com.illegalaccess.delay.core.business.DelayServerBusiness;
import com.illegalaccess.delay.toolkit.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/delay/inner")
public class DelayMessageInnerController {

    @Autowired
    private DelayServerBusiness delayServerBusiness;
    @Autowired
    private DelayCoreProperties delayCoreProperties;

    @PostMapping("/cancelDelayMessage")
    public String cancelDelayMessage(@RequestHeader("access-token") String accessToken, @RequestHeader("access-time") long accessTime, @RequestBody CancelMessageReq req) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime accessDate = TimeUtils.toLocalDateTime(accessTime);
        Duration duration = Duration.between(accessDate, now);
        long mills = duration.toMillis();
        if (mills <= 0 || mills > delayCoreProperties.getEffectiveAccessInterval()) {
            log.info("access time:{} is invalid,now:{}", accessDate, now);
            return "";
        }

        if (!delayCoreProperties.getAccessToken().equals(accessToken)) {
            log.info("access token:{} is invalid", accessToken);
            return "";
        }

        return delayServerBusiness.cancelMessage(req);
    }
}
