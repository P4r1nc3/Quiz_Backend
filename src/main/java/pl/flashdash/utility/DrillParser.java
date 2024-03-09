package pl.flashdash.utility;

import pl.flashdash.model.QuestionDrill;

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

    public static List<QuestionDrill> parseDrillFile(Path filePath) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        List<QuestionDrill> questionDrills = new ArrayList<>();
        QuestionDrill currentQuestionDrill = null;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith(">>>")) {
                processCorrectAnswer(currentQuestionDrill, line);
            } else if (line.matches("^[a-d]\\) .+")) {
                processAnswer(currentQuestionDrill, line);
            } else {
                currentQuestionDrill = processQuestion(questionDrills, currentQuestionDrill, line);
            }
        }

        if (currentQuestionDrill != null && isValidQuestion(currentQuestionDrill)) {
            questionDrills.add(currentQuestionDrill);
        }

        return questionDrills;
    }

    private static void processCorrectAnswer(QuestionDrill questionDrill, String line) {
        if (questionDrill == null) return;
        Matcher matcher = CORRECT_ANSWER_PATTERN.matcher(line);
        if (matcher.matches()) {
            questionDrill.getCorrectAnswers().add(matcher.group(2).trim());
        }
    }

    private static void processAnswer(QuestionDrill questionDrill, String line) {
        if (questionDrill == null) return;
        Matcher matcher = ANSWER_PATTERN.matcher(line);
        if (matcher.matches()) {
            questionDrill.getIncorrectAnswers().add(matcher.group(2).trim());
        }
    }

    private static QuestionDrill processQuestion(List<QuestionDrill> questionDrills, QuestionDrill currentQuestionDrill, String line) {
        if (currentQuestionDrill != null && isValidQuestion(currentQuestionDrill)) {
            questionDrills.add(currentQuestionDrill);
        }
        QuestionDrill newQuestionDrill = new QuestionDrill();
        newQuestionDrill.setQuestion(line);
        return newQuestionDrill;
    }

    private static boolean isValidQuestion(QuestionDrill questionDrill) {
        return questionDrill.getQuestion() != null && !questionDrill.getCorrectAnswers().isEmpty();
    }
}
