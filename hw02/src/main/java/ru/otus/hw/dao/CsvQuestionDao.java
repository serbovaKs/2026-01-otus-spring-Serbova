package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class CsvQuestionDao implements QuestionDao {
    private final AppProperties appProperties;

    @Override
    public List<Question> findAll() {
        var resource = getClass().getClassLoader().getResource(appProperties.getTestFileName());
        try (Reader reader = Files.newBufferedReader(Paths.get(Objects.requireNonNull(resource).toURI()))) {
            List<QuestionDto> beans = new CsvToBeanBuilder(reader)
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .withSeparator(';')
                    .build()
                    .parse();
            return beans.stream().map(QuestionDto::toDomainObject).toList();
        } catch (URISyntaxException | IOException e) {
            throw new QuestionReadException("Error read csv file", e);
        }
    }
}
