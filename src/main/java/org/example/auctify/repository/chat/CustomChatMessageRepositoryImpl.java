package org.example.auctify.repository.chat;

import static org.springframework.data.mongodb.core.query.Criteria.*;

import lombok.RequiredArgsConstructor;
import org.example.auctify.entity.chat.ChatMessageEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomChatMessageRepositoryImpl implements CustomChatMessageRepository {
	private final MongoTemplate mongoTemplate;

	@Override
	public void updateUnReadCount(Long chatRoomId, Long userId) {
		Query query = new Query(where("chatRoomId").is(chatRoomId)
				.and("userId").ne(userId)
				.and("unReadCount").gt(0)); // unReadCount가 0보다 큰 경우만 조회

		Update update = new Update().inc("unReadCount", -1);

		mongoTemplate.updateMulti(query, update, ChatMessageEntity.class);
	}
}
