package uk.gov.hmcts.reform.iacasedocumentsapi.infrastructure;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.iacasedocumentsapi.domain.DateProvider;

@Service
public class SystemDateProvider implements DateProvider {

    public LocalDate now() {
        return LocalDate.now();
    }

    public LocalDateTime nowWithTime() {
        return LocalDateTime.now();
    }
}
