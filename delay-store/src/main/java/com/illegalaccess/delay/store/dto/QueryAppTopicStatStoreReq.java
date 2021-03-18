package com.illegalaccess.delay.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryAppTopicStatStoreReq implements Serializable {

    private static final long serialVersionUID = -1L;

    private String appKey;

    private List<String> topics;

    private LocalDateTime start;

    private LocalDateTime end;
}
