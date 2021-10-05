package bitcoin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        // get list of DailyPortFolio objects with total number of bitcoins as their value.
        List<DailyPortfolioValue> totalBitcoinsPerDay = getTotalBitcoinsPerDay();
        List<DailyPortfolioValue> dailyPortfolioValues = new ArrayList<>();

        // generate a new list of DailyPortFolio objects by iterating over the list totalBitcoinsPerDay.
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

    // generate a new list of dates for days 1st - 7th September
    public static List<LocalDate> getListOfDates() {

        List<LocalDate> days = new ArrayList<>();

        int i;
        for (i=0; i<=7-1; i++) {
            LocalDate newDay = LocalDate.of(2021, Month.SEPTEMBER, i+1);
            days.add(newDay);
        }
        return days;
    }

    // generate a new list of DailyPortFolioValue objects by calling a method to generate a list of LocalDates then iterate over the TRANSACTIONS list to get the total bitcoins for each day.
    public static List<DailyPortfolioValue> getTotalBitcoinsPerDay() {

        List<LocalDate> days = getListOfDates();
        List<DailyPortfolioValue> totalBitcoinsPerDay = new ArrayList<>();

        for (LocalDate day : days) {
            BigDecimal runningTotal = new BigDecimal(0);

            // get a list of Transaction objects, filtered by the dayOfMonth of the current day.
            // use a stream to filter and then collect into a list.
            // collection will hold all Transactions up to and including the current day.
            List<Transaction> filteredTransactions = TRANSACTIONS.stream()
                    .filter(transaction -> transaction.effectiveDate().getDayOfMonth() <= day.getDayOfMonth())
                    .collect(Collectors.toList());

            // iterate over the filtered list to sum the total number of bitcoins for the current day
            for (Transaction transaction : filteredTransactions) {

                // runningTotal is the cumulative number of bitcoins, carried over each day and affected by transactions.
                // It is a BigDecimal object which is immutable. Therefore, it is reassigned and given the result of this calculation each time it is performed.
                runningTotal = runningTotal.add(transaction.numberOfBitcoins());
            }
            totalBitcoinsPerDay.add(new DailyPortfolioValue(day, runningTotal));
        }
        return totalBitcoinsPerDay;
    }
}
