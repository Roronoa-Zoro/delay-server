package com.illegalaccess.delay.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 查询系统负载的请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryDelayMessageSnapshotReq implements Serializable {

    private static final long serialVersionUID = -1L;

    private List<String> ipList;

    private LocalDateTime start;

    private LocalDateTime end;
}
