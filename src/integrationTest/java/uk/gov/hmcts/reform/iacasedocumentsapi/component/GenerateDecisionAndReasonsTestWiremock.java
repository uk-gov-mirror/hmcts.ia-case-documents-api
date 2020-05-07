package uk.gov.hmcts.reform.iacasedocumentsapi.component;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.reform.iacasedocumentsapi.domain.entities.AsylumCaseDefinition.*;
import static uk.gov.hmcts.reform.iacasedocumentsapi.domain.entities.DocumentTag.DECISION_AND_REASONS_DRAFT;
import static uk.gov.hmcts.reform.iacasedocumentsapi.domain.entities.ccd.State.DECISION;
import static uk.gov.hmcts.reform.iacasedocumentsapi.infrastructure.testutils.fixtures.AsylumCaseForTest.anAsylumCase;
import static uk.gov.hmcts.reform.iacasedocumentsapi.infrastructure.testutils.fixtures.CallbackForTest.CallbackForTestBuilder.callback;
import static uk.gov.hmcts.reform.iacasedocumentsapi.infrastructure.testutils.fixtures.CaseDetailsForTest.CaseDetailsForTestBuilder.someCaseDetailsWith;
import static uk.gov.hmcts.reform.iacasedocumentsapi.infrastructure.testutils.fixtures.UserDetailsForTest.UserDetailsForTestBuilder.userWith;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import uk.gov.hmcts.reform.iacasedocumentsapi.component.testutils.WiremockSpringBootIntegrationTest;
import uk.gov.hmcts.reform.iacasedocumentsapi.domain.entities.DocumentWithMetadata;
import uk.gov.hmcts.reform.iacasedocumentsapi.domain.entities.ccd.Event;
import uk.gov.hmcts.reform.iacasedocumentsapi.domain.entities.ccd.field.IdValue;
import uk.gov.hmcts.reform.iacasedocumentsapi.domain.entities.ccd.field.YesOrNo;
import uk.gov.hmcts.reform.iacasedocumentsapi.infrastructure.testutils.fixtures.PreSubmitCallbackResponseForTest;

public class GenerateDecisionAndReasonsTestWiremock extends WiremockSpringBootIntegrationTest {

    @Test
    public void generates_decision_and_reasons() {

        given.someLoggedIn(userWith()
            .roles(newHashSet("caseworker-ia", "caseworker-ia-caseofficer"))
            .id("1")
            .email("some-email@email.com")
            .forename("Case")
            .surname("Officer"));

        given.docmosisWillReturnSomeDocument();
        given.theDocoumentsManagementApiIsAvailable();

        PreSubmitCallbackResponseForTest response = iaCaseDocumentsApiClient.aboutToSubmit(callback()
            .event(Event.GENERATE_DECISION_AND_REASONS)
            .caseDetails(someCaseDetailsWith()
                .id(1)
                .state(DECISION)
                .createdDate(LocalDateTime.now())
                .jurisdiction("IA")
                .caseData(anAsylumCase()
                    .with(APPEAL_REFERENCE_NUMBER, "some-appeal-reference-number")
                    .with(APPELLANT_FAMILY_NAME, "some-fname")
                    .with(APPELLANT_GIVEN_NAMES, "some-gname")
                    .with(APPEAL_REFERENCE_NUMBER, "some-appeal-reference-number")
                    .with(CASE_INTRODUCTION_DESCRIPTION, "some-case-intro")
                    .with(APPELLANT_CASE_SUMMARY_DESCRIPTION, "some-case-summary-description")
                    .with(IMMIGRATION_HISTORY_AGREEMENT, YesOrNo.YES)
                    .with(AGREED_IMMIGRATION_HISTORY_DESCRIPTION, "some-agreed-immigration-description")
                    .with(SCHEDULE_OF_ISSUES_AGREEMENT, "Yes")
                    .with(APPELLANTS_AGREED_SCHEDULE_OF_ISSUES_DESCRIPTION, "some-agreed-schedule-of-issues-description")
                    .with(DECISION_AND_REASONS_AVAILABLE, YesOrNo.NO)
                    .with(ANONYMITY_ORDER, YesOrNo.YES)
                    .with(APPELLANT_REPRESENTATIVE, "ted")
                    .with(RESPONDENT_REPRESENTATIVE, "bill")
                    .with(LIST_CASE_HEARING_CENTRE, "taylorHouse")

                )));

        Optional<List<IdValue<DocumentWithMetadata>>> draftDecisionAndReasonsDocuments =
            response.getAsylumCase().read(DRAFT_DECISION_AND_REASONS_DOCUMENTS);

        IdValue<DocumentWithMetadata> documentWithMetadataIdValue = draftDecisionAndReasonsDocuments.get().get(0);

        assertThat(draftDecisionAndReasonsDocuments.get().size()).isEqualTo(1);
        assertThat(documentWithMetadataIdValue.getValue().getTag()).isEqualTo(DECISION_AND_REASONS_DRAFT);
    }
}
