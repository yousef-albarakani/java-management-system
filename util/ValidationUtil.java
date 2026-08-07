package util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class ValidationUtil {

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^\\+?[0-9\\-]{10,15}$");
    }

    public static boolean isValidAge(int age) {
        return age >= 18 && age <= 100;
    }

    public static boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 5;
    }

    public static boolean isWeekday(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    public static boolean isValidWorkingTime(LocalTime time) {
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 0);

        return !time.isBefore(start) && !time.isAfter(end);
    }

    public static boolean isValidAppointmentDate(LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDate endOfNextWeek = today.plusWeeks(1).with(DayOfWeek.FRIDAY);

        return !date.isBefore(today) && !date.isAfter(endOfNextWeek);
    }

    public static boolean isValidAppointmentDateTime(LocalDate date, LocalTime time) {
        return isWeekday(date) && isValidWorkingTime(time) && isValidAppointmentDate(date);
    }
}