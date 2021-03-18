package com.illegalaccess.delay.ui.client.dto.app;

import com.illegalaccess.delay.toolkit.dto.PageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryAppKeyResp extends PageResponse {

    List<QueryAppKeyInfo> data;
}
