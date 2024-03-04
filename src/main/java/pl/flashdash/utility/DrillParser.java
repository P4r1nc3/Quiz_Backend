package pl.flashdash.utility;

import pl.flashdash.model.Question;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DrillParser {

    private static final Pattern CORRECT_ANSWER_PATTERN = Pattern.compile("^>>>([a-d])\\) (.+)");
    private static final Pattern ANSWER_PATTERN = Pattern.compile("^([a-d])\\) (.+)");

    public static List<Question> parseDrillFile(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        List<Question> questions = new ArrayList<>();
        Question currentQuestion = null;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith(">>>")) {
                processCorrectAnswer(currentQuestion, line);
            } else if (line.matches("^[a-d]\\) .+")) {
                processAnswer(currentQuestion, line);
            } else {
                currentQuestion = processQuestion(questions, currentQuestion, line);
            }
        }

        if (currentQuestion != null && isValidQuestion(currentQuestion)) {
            questions.add(currentQuestion);
        }

        return questions;
    }

    private static void processCorrectAnswer(Question question, String line) {
        if (question == null) return;
        Matcher matcher = CORRECT_ANSWER_PATTERN.matcher(line);
        if (matcher.matches()) {
            question.getCorrectAnswers().add(matcher.group(2).trim());
        }
    }

    private static void processAnswer(Question question, String line) {
        if (question == null) return;
        Matcher matcher = ANSWER_PATTERN.matcher(line);
        if (matcher.matches()) {
            question.getIncorrectAnswers().add(matcher.group(2).trim());
        }
    }

    private static Question processQuestion(List<Question> questions, Question currentQuestion, String line) {
        if (currentQuestion != null && isValidQuestion(currentQuestion)) {
            questions.add(currentQuestion);
        }
        Question newQuestion = new Question();
        newQuestion.setQuestion(line);
        return newQuestion;
    }

    private static boolean isValidQuestion(Question question) {
        return question.getQuestion() != null && !question.getCorrectAnswers().isEmpty();
    }
}
