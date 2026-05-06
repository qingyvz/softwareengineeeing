package com.oriole.wisepen.search.domain.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 接收上游：ACL 重新计算事件消息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AclRecalculateMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 注意：这里必须是 String 类型以对齐上游 */
    private String resourceId;

    private String triggerSource;
}