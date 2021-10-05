package bitcoin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        List<DailyPortfolioValue> dailyPortfolioValues = new ArrayList<>();

        // generate a new list of dates for days 1st - 7th September
        List<LocalDate> days = new ArrayList<>();
        int i;
        for (i=0; i<=7-1; i++) {
           LocalDate newDay = LocalDate.of(2021, Month.SEPTEMBER, i+1);
           days.add(newDay);
        }

        // generate a new list of DailyPortFolioValue objects, using dates from days ArrayList and taking values from TRANSACTIONS to get amount of bitcoin for each day.
        List<DailyPortfolioValue> dailyPortFolioCoins = new ArrayList<>();

//        // generate a Map with the LocalDate as key and the BigDecimal object runningTotal as value. This allows the cumulative total of bitcoins to be stored alongside the respective date.
//          Map<LocalDate, BigDecimal> dailyPortFolioCoins = new HashMap<>();

        for (LocalDate day : days) {
            BigDecimal runningTotal = new BigDecimal(0);

            // create stream to filter TRANSACTIONS array by current date of iterated objected and collect into a List
            List<Transaction> filteredTransactions = TRANSACTIONS.stream()
                    .filter(transaction -> transaction.effectiveDate().getDayOfMonth() <= day.getDayOfMonth())
                    .collect(Collectors.toList());
            // runningTotal needs to be final, can't be reassigned.
//                  .forEach(transaction -> runningTotal = runningTotal.add(transaction.numberOfBitcoins()));

            // iterate over filtered List to sum the total number of bitcoins for the current day
            for (Transaction transaction : filteredTransactions) {
                runningTotal = runningTotal.add(transaction.numberOfBitcoins());
            }

            dailyPortFolioCoins.add(new DailyPortfolioValue(day, runningTotal));
        }

//        for (Map.Entry<LocalDate, BigDecimal> entry : dailyPortFolioCoins.entrySet()) {
//            LocalDate key = entry.getKey();
//            BigDecimal value = entry.getValue();
//            BigDecimal actualValue = new BigDecimal(0);
//
//            for (Price price : PRICES) {
//                if (key.getDayOfMonth() <= price.effectiveDate().getDayOfMonth()) {
//                    actualValue = value.multiply(price.price());
//                }
//            }
//
//            dailyPortfolioValues.add(new DailyPortfolioValue(key, actualValue));
//        }

        // generate a new list of DailyPortFolio objects by iterating over dailyPortFolioCoins, for each object iterate over PRICES list and where dates match, multiply value by price.
        for (DailyPortfolioValue dailyPortfolioCoin : dailyPortFolioCoins) {
            BigDecimal currentValue = new BigDecimal(0);

            // as currentValue is reassigned each time and not cumulative, and the objects are in ascending order by date, the latest price will be used where multiple entries for a date are present.
            for (Price currentPrice : PRICES) {
                if (currentPrice.effectiveDate().getDayOfMonth() <= dailyPortfolioCoin.date().getDayOfMonth()) {
                    currentValue = dailyPortfolioCoin.value().multiply(currentPrice.price());
                }

            }
            dailyPortfolioValues.add(new DailyPortfolioValue(dailyPortfolioCoin.date(), currentValue));
        }

//        for (LocalDate day : days) {
//            int j = 0;
//            while (day.getDayOfMonth() <= TRANSACTIONS.get(j).effectiveDate().getDayOfMonth()) {
//                runningTotal = runningTotal.add(TRANSACTIONS.get(j).numberOfBitcoins());
//                j++;
//            }
//            DailyPortfolioValue dailyPortFolioCoin = new DailyPortfolioValue(day, runningTotal);
//            dailyPortFolioCoins.add(dailyPortFolioCoin);
//        }

//            for (Transaction transaction : TRANSACTIONS) {
//                if (day.getDayOfMonth() <= transaction.effectiveDate().getDayOfMonth()) {
//                    runningTotal = runningTotal.add(transaction.numberOfBitcoins());
//                    DailyPortfolioValue dailyPortFolioCoin = new DailyPortfolioValue(day, runningTotal);
//                    dailyPortFolioCoins.add(dailyPortFolioCoin);
//                }
//
//                else {
//                    DailyPortfolioValue dailyPortFolioCoin = new DailyPortfolioValue(day, runningTotal);
//                    dailyPortFolioCoins.add(dailyPortFolioCoin);
//                }
//            }
//        }

//        List<DailyPortfolioValue> dailyPortfolioValues = new ArrayList<>();
//        for (DailyPortfolioValue dailyPortFolioCoin : dailyPortFolioCoins) {
//            BigDecimal endOfDayValue = new BigDecimal(0);
//
//            int j;
//            for (j=0; j<PRICES.size(); j++) {
//                if (PRICES.get(j).effectiveDate().getDayOfMonth() == dailyPortFolioCoin.date().getDayOfMonth()
//                        && PRICES.get(j+1).effectiveDate().getDayOfMonth() != dailyPortFolioCoin.date().getDayOfMonth()) {
//                    endOfDayValue = dailyPortFolioCoin.value().multiply(PRICES.get(j).price());
//                }
//            }
//            dailyPortfolioValues.add(new DailyPortfolioValue(dailyPortFolioCoin.date(), endOfDayValue));
//        }

        return dailyPortfolioValues;
    }
}
