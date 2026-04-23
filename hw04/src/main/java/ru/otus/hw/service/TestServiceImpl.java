package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            ioService.printFormattedLine(question.text());
            int numCorrectAnswer = getNumCorrectAnswer(question.answers());
            int numAnswer = ioService.readIntForRangeLocalized(
                    1,
                    question.answers().size(),
                    "TestService.error.num.answer"
            );
            boolean isAnswerValid = numCorrectAnswer == numAnswer;
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private int getNumCorrectAnswer(List<Answer> answers) {
        var numCorrectAnswer = 0;
        for (int index = 0; index < answers.size(); index++) {
            ioService.printLine("%s - %s".formatted(index + 1, answers.get(index).text()));
            if (answers.get(index).isCorrect()) {
                numCorrectAnswer = index + 1;
            }
        }
        return numCorrectAnswer;
    }

}
