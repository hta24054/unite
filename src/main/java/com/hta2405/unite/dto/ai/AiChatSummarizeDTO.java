package com.hta2405.unite.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AiChatSummarizeDTO {
    private String topic;
    private String summary;
}
