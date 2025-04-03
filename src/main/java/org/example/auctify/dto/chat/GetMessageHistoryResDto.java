package org.example.auctify.dto.chat;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMessageHistoryResDto {
	private List<String> contents;
	private List<LocalDateTime> sendTimes;
	private String goodsName;
}
