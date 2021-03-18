package com.illegalaccess.delay.ui.client.dto.app;

import com.illegalaccess.delay.toolkit.dto.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryAppKeyReq extends PageRequest {

    private String creator;
}
