package com.illegalaccess.delay.ui.client.dto.system;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class QueryServerLoadReq implements Serializable {

    private List<String> hostIpList;

    private LocalDateTime start;

    private LocalDateTime end;
}
