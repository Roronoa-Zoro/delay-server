package com.illegalaccess.delay.ui.client.dto.topic;

import lombok.*;

import java.io.Serializable;
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveTopicResp implements Serializable {

    private static final long serialVersionUID = -1L;

    private Long id;
}
