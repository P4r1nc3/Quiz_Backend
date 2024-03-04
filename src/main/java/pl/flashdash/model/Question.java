package pl.flashdash.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private String question;
    private List<String> correctAnswers = new ArrayList<>();
    private List<String> incorrectAnswers = new ArrayList<>();
}
