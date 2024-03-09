package pl.flashdash.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.flashdash.model.question.Question;
import pl.flashdash.utility.ControllerUtils;
import pl.flashdash.utility.DrillParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/drill")
public class DrillController {

    @PostMapping("/upload")
    public ResponseEntity<?> uploadQuiz(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try {
            Path tempFile = Files.createTempFile(null, null);
            file.transferTo(tempFile);

            List<Question> questions = DrillParser.parseDrillFile(tempFile);

            if (questions.isEmpty()) {
                return ControllerUtils.createErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ControllerUtils.BAD_REQUEST_CAUSE,
                        ControllerUtils.BAD_REQUEST_ACTION,
                        request
                );
            }

            return ResponseEntity.ok(questions);
        }
        catch (Exception e) {
            return ControllerUtils.createErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ControllerUtils.BAD_REQUEST_CAUSE,
                    ControllerUtils.BAD_REQUEST_ACTION,
                    request
            );
        }
    }
}
