package pl.flashdash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.flashdash.model.Deck;
import pl.flashdash.model.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByDeck(Deck deck);
    Optional<Question> findByDeckAndQuestionId(Deck deck, Long cardId);
    void deleteAllByDeck(Deck deck);
}
