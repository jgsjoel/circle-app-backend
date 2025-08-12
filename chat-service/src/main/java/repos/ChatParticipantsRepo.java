package repos;

import com.chat.chat.entities.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatParticipantsRepo extends JpaRepository<ChatParticipant,Long> {

    List<ChatParticipant> findAllByUserId(String userId);
    boolean existByChatIdAndUserId(String chatId,String second);

}
