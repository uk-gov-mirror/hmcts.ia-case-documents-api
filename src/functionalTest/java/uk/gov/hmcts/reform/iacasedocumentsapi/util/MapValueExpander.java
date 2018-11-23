package uk.gov.hmcts.reform.iacasedocumentsapi.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public final class MapValueExpander {

    private static final Pattern TODAY_PATTERN = Pattern.compile("\\{\\$TODAY([+-]?\\d*?)}");

    private MapValueExpander() {
        // noop
    }

    public static void expandValues(Map<String, Object> map) {

        for (Map.Entry<String, Object> entry : map.entrySet()) {

            Object untypedValue = entry.getValue();

            if (untypedValue instanceof List) {

                untypedValue =
                    ((List) untypedValue)
                        .stream()
                        .map(MapValueExpander::expandValue)
                        .collect(Collectors.toList());

            } else {
                untypedValue = expandValue(untypedValue);
            }

            entry.setValue(untypedValue);
        }
    }

    private static Object expandValue(Object untypedValue) {

        if (untypedValue instanceof Map) {

            expandValues((Map<String, Object>) untypedValue);

        } else if (untypedValue instanceof String) {

            String value = (String) untypedValue;

            if (TODAY_PATTERN.matcher(value).matches()) {
                value = expandToday(value);
            }

            return value;
        }

        return untypedValue;
    }

    private static String expandToday(String value) {

        Matcher matcher = TODAY_PATTERN.matcher(value);

        String expandedValue = value;

        while (matcher.find()) {

            char plusOrMinus = '+';
            int dayAdjustment = 0;

            if (matcher.groupCount() == 1
                && !matcher.group(1).isEmpty()) {

                plusOrMinus = matcher.group(1).charAt(0);
                dayAdjustment = Integer.valueOf(matcher.group(1).substring(1));
            }

            LocalDate now = LocalDate.now();

            if (plusOrMinus == '+') {
                now = now.plusDays(dayAdjustment);
            } else {
                now = now.minusDays(dayAdjustment);
            }

            String token = matcher.group(0);

            expandedValue = expandedValue.replace(token, now.toString());
        }

        return expandedValue;
    }
}
