package com.illegalaccess.delay.store.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.illegalaccess.delay.store.entity.DelayMessage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 延时消息表 Mapper 接口
 * </p>
 *
 * @author Jimmy Li
 * @since 2021-02-03
 */
public interface DelayMessageMapper extends BaseMapper<DelayMessage> {

    @Update("update delay_message set status=#{status} where msg_id=#{messageId}")
    boolean updateDelayMessage(@Param("messageId") String messageId, @Param("status") Integer status);
}
