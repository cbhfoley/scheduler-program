package utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class dateTimeUtils {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Returns the current timestamp in the format of yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(dateTimeFormatter);
    }

    /**
     * Converts a timestamp passed to it to UTC.
     *
     * @param localTimestamp
     * @return
     */
    public static String convertToUTC(String localTimestamp) {
        LocalDateTime localDateTime = LocalDateTime.parse(localTimestamp, dateTimeFormatter);

        // Convert local timestamp to UTC
        ZonedDateTime localZonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZonedDateTime = localZonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));

        return utcZonedDateTime.format(dateTimeFormatter);
    }

    /**
     * Converts a timestamp passed to it to ET.
     *
     * @param localTimestamp
     * @return
     */
    public static String convertToEastern(String localTimestamp) {
        LocalDateTime localDateTime = LocalDateTime.parse(localTimestamp, dateTimeFormatter);

        // Convert local timestamp to Eastern Time
        ZonedDateTime localZonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZonedDateTime = localZonedDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));

        return utcZonedDateTime.format(dateTimeFormatter);
    }

    /**
     * Converts a UTC timestamp passed to it to the local users time.
     *
     * @param utcTimestamp
     * @return
     */
    public static String convertToLocal(String utcTimestamp) {
        LocalDateTime utcDateTime = LocalDateTime.parse(utcTimestamp, dateTimeFormatter);

        // Convert UTC timestamp to local time
        ZonedDateTime utcZonedDateTime = utcDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime localZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault());

        return localZonedDateTime.format(dateTimeFormatter);
    }

    /**
     * Combines the date and times selected by the user in the appointment options for adding to the database and returns
     * it.
     *
     * @param date
     * @param time
     * @return
     */
    public static String combineDateTime(LocalDate date, String time) {
        LocalDateTime combinedDateTime = LocalDateTime.of(date, LocalTime.parse(time));
        return combinedDateTime.format(dateTimeFormatter);
    }

    /**
     * Accepts the start and end time for an appointment and verifies if it's within the defined business hours.
     *
     * @param selectedStartDate
     * @param startTime
     * @param selectedEndDate
     * @param endTime
     * @return
     */
    public static boolean isWithinBusinessHours(LocalDate selectedStartDate, String startTime, LocalDate selectedEndDate, String endTime) {
        // Business hours are defined between 8am and 10pm. 07:59 and 22:01 include 8am and 10pm.
        LocalTime businessStartTime = LocalTime.of(7, 59);
        LocalTime businessEndTime = LocalTime.of(22, 1);

        LocalDateTime startAppointmentTime = LocalDateTime.parse(convertToEastern(combineDateTime(selectedStartDate, startTime)), dateTimeFormatter);
        LocalDateTime endAppointmentTime = LocalDateTime.parse(convertToEastern(combineDateTime(selectedEndDate, endTime)), dateTimeFormatter);

        return startAppointmentTime.toLocalTime().isAfter(businessStartTime) && endAppointmentTime.toLocalTime().isBefore(businessEndTime);
    }

    /**
     * Accepts the start and end time for an appointment and verifies the start time is before the end time.
     *
     * @param startDate
     * @param startTime
     * @param endDate
     * @param endTime
     * @return
     */
    public static boolean isStartBeforeEnd(LocalDate startDate, String startTime, LocalDate endDate, String endTime) {
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);

        // Check if start date is before end date. If so it does not need to check the times and returns true.
        if (startDate.isBefore(endDate)) {
            return true;
        }
        // If they are equal (most likely scenario) check that the start time is before the end time
        else if (startDate.isEqual(endDate)) {
            return start.isBefore(end);
        }
        // If neither are true, return false.
        else {
            return false;
        }
    }
}
