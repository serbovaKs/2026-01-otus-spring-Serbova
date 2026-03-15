package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        return createTestResult(questionDao.findAll(), student);
    }

    private TestResult createTestResult(List<Question> questions, Student student) {
        var testResult = new TestResult(student);
        for (var question: questions) {
            var isAnswerValid = false;
            ioService.printFormattedLine(question.text());
            List<Answer> answers = question.answers();
            int numCorrectAnswer = 0;
            for (int index = 0; index < answers.size(); index++) {
                ioService.printLine("%s - %s".formatted(index + 1, answers.get(index).text()));
                if (answers.get(index).isCorrect()) {
                    numCorrectAnswer = index + 1;
                }
            }
            int numAnswer = ioService.readIntForRange(
                    1,
                    question.answers().size(),
                    "Selected answer number is outside the allowed range"
            );
            isAnswerValid = numCorrectAnswer == numAnswer;
            testResult.applyAnswer(question, isAnswerValid);
        }
        return  testResult;
    }
}
