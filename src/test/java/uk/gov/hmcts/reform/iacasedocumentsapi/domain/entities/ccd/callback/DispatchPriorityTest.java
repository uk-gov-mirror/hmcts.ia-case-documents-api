package uk.gov.hmcts.reform.iacasedocumentsapi.domain.entities.ccd.callback;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

public class DispatchPriorityTest {

    @Test
    public void has_correct_case_event_ids() {
        assertEquals("early", DispatchPriority.EARLY.toString());
        assertEquals("late", DispatchPriority.LATE.toString());
    }

    @Test
    public void if_this_test_fails_it_is_because_it_needs_updating_with_your_changes() {
        assertEquals(2, DispatchPriority.values().length);
    }
}
