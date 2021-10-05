package bitcoin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class PortfolioPerformance {

    private static final List<Price> PRICES = List.of(
            new Price(LocalDateTime.of(2021, Month.SEPTEMBER, 1, 5, 0, 0), new BigDecimal("35464.53")),
            new Price(LocalDateTime.of(2021, Month.SEPTEMBER, 2, 5, 0, 0), new BigDecimal("35658.76")),
            new Price(LocalDateTime.of(2021, Month.SEPTEMBER, 3, 5, 0, 0), new BigDecimal("36080.06")),
            new Price(LocalDateTime.of(2021, Month.SEPTEMBER, 3, 13, 0, 0), new BigDecimal("37111.11")),
            new Price(LocalDateTime.of(2021, Month.SEPTEMBER, 6, 5, 0, 0), new BigDecimal("38041.47")),
            new Price(LocalDateTime.of(2021, Month.SEPTEMBER, 7, 5, 0, 0), new BigDecimal("34029.61")));

    private static final List<Transaction> TRANSACTIONS = List.of(
            new Transaction(LocalDateTime.of(2021, Month.SEPTEMBER, 1, 9, 0, 0), new BigDecimal("0.012")),
            new Transaction(LocalDateTime.of(2021, Month.SEPTEMBER, 1, 15, 0, 0), new BigDecimal("-0.007")),
            new Transaction(LocalDateTime.of(2021, Month.SEPTEMBER, 4, 9, 0, 0), new BigDecimal("0.017")),
            new Transaction(LocalDateTime.of(2021, Month.SEPTEMBER, 5, 9, 0, 0), new BigDecimal("-0.01")),
            new Transaction(LocalDateTime.of(2021, Month.SEPTEMBER, 7, 9, 0, 0), new BigDecimal("0.1")));

    // Complete this method to return a list of daily portfolio values with one record for each day from the 01-09-2021-07-09-2021 in ascending date order
    public static List<DailyPortfolioValue> getDailyPortfolioValues() {

        List<DailyPortfolioValue> totalBitcoinsPerDay = getTotalBitcoinsPerDay();
        List<DailyPortfolioValue> dailyPortfolioValues = new ArrayList<>();

        // For each element, iterate over the PRICES list and where currentPrice dayOfTheMonth is less than or equal to the current element's, multiply value by price.
        for (DailyPortfolioValue element : totalBitcoinsPerDay) {
            BigDecimal currentValue = new BigDecimal(0);

            // currentValue is reassigned each time, not cumulative and the objects are in ascending order by date.
            // The latest price will be used where multiple entries for a date are present because it will be the last calculation performed.
            for (Price currentPrice : PRICES) {
                if (currentPrice.effectiveDate().getDayOfMonth() <= element.date().getDayOfMonth()) {
                    currentValue = element.value().multiply(currentPrice.price());
                }
            }
            dailyPortfolioValues.add(new DailyPortfolioValue(element.date(), currentValue));
        }
        return dailyPortfolioValues;
    }

    // generate a list of DailyPortFolioValue objects, each with the total number of bitcoins per day as their value.
    public static List<DailyPortfolioValue> getTotalBitcoinsPerDay() {

        List<LocalDate> days = getListOfDates();
        List<DailyPortfolioValue> totalBitcoinsPerDay = new ArrayList<>();

        for (LocalDate day : days) {
            BigDecimal runningTotal = new BigDecimal(0);

            // iterate over TRANSACTIONS to sum the total number of bitcoins for the current day
            for (Transaction transaction : TRANSACTIONS) {
                if (transaction.effectiveDate().getDayOfMonth() <= day.getDayOfMonth()) {

                    // runningTotal is a BigDecimal object, which are immutable. Therefore, it is reassigned and given the result of this calculation each time it is performed.
                    runningTotal = runningTotal.add(transaction.numberOfBitcoins());
                }
            }
            totalBitcoinsPerDay.add(new DailyPortfolioValue(day, runningTotal));
        }
        return totalBitcoinsPerDay;
    }

    // generate a new list of LocalDate objects for days 1st - 7th September
    public static List<LocalDate> getListOfDates() {

        List<LocalDate> days = new ArrayList<>();

        int i;
        for (i=0; i<=7-1; i++) {
            LocalDate newDay = LocalDate.of(2021, Month.SEPTEMBER, i+1);
            days.add(newDay);
        }
        return days;
    }
}
