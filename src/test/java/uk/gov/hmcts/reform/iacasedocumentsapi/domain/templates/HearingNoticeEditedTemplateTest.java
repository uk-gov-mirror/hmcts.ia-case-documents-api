package uk.gov.hmcts.reform.iacasedocumentsapi.domain.templates;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.iacasedocumentsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasedocumentsapi.domain.entities.ccd.CaseDetails;
import uk.gov.hmcts.reform.iacasedocumentsapi.domain.service.StringProvider;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class HearingNoticeEditedTemplateTest {

    private final String templateName = "HEARING_NOTICE_TEMPLATE.docx";

    @Mock private StringProvider stringProvider;
    @Mock private CaseDetails<AsylumCase> caseDetails;
    @Mock private CaseDetails<AsylumCase> caseDetailsBefore;
    @Mock private HearingNoticeUpdatedTemplateProvider hearingNoticeUpdatedTemplateProvider;

    private HearingNoticeEditedTemplate hearingNoticeEditedTemplate;

    @Before
    public void setUp() {

        hearingNoticeEditedTemplate =
            new HearingNoticeEditedTemplate(
                templateName,
                stringProvider,
                hearingNoticeUpdatedTemplateProvider
            );
    }

    @Test
    public void should_return_template_name() {

        assertEquals(templateName, hearingNoticeEditedTemplate.getName());
    }

    @Test
    public void should_map_case_data_to_template_field_values() {

        hearingNoticeUpdatedTemplateProvider.mapFieldValues(caseDetails, caseDetailsBefore);

        verify(hearingNoticeUpdatedTemplateProvider, times(1)).mapFieldValues(caseDetails, caseDetailsBefore);
    }
}
