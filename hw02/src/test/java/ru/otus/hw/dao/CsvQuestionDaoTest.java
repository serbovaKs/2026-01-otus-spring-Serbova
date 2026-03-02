package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.domain.Question;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class CsvQuestionDaoTest {
    private final AppProperties appProperties = mock();

    private final CsvQuestionDao questionDao = new CsvQuestionDao(appProperties);

    @TempDir
    Path tempDir;

    @Test
    void findAll_Test() throws IOException {
        Path tempFile = tempDir.resolve("questions.csv");
        Files.copy(Paths.get("src\\main\\resources\\questions.csv"), tempFile, StandardCopyOption.REPLACE_EXISTING);
        List<String> listQuestion = Files.readAllLines(tempFile, StandardCharsets.UTF_8);
        listQuestion.remove(0);
        listQuestion = listQuestion.stream()
                .map(str -> str.substring(0, str.indexOf(";")))
                .toList();

        when(appProperties.getTestFileName()).thenReturn("questions.csv");

        List<Question> list = questionDao.findAll();
        Assertions.assertEquals(list.stream().map(Question::text).toList(), listQuestion);
    }
}
