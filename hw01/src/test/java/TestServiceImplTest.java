import org.junit.jupiter.api.Test;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.TestServiceImpl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TestServiceImplTest {
    private final IOService ioService = mock();
    private final QuestionDao questionDao = mock();

    private final TestServiceImpl testService = new TestServiceImpl(ioService, questionDao);

    @Test
    void executeTest_Test() {
        doNothing().when(ioService).printLine(anyString());
        doNothing().when(ioService).printFormattedLine(anyString());
        doNothing().when(ioService).printFormattedLine(anyString(), anyString());
        when(questionDao.findAll()).thenReturn(anyList());

        testService.executeTest();
    }
}
