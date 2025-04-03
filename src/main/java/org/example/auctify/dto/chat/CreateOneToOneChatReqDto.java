package org.example.auctify.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOneToOneChatReqDto {
	private Long theOtherUserId;
	private Long goodsId;
}
