package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages exchange rates amongst various currencies.
 */
public class ExchangeRate {
    /**
     * A {@link Map} of currencies (e.g., US Dollars, Euros, etc.) to their
     * corresponding exchange rates compared with other currencies.
     */
    private final Map<String, Map<String, Double>> mExchangeRate =
        new HashMap<>();

    /**
     * The constructor initializes the {@link Map}.
     */
    public ExchangeRate() {
        Map<String, Double> usdExchangeRates = new HashMap<>();
        usdExchangeRates.put("USD", 1.0);
        usdExchangeRates.put("GBP", 0.72);
        usdExchangeRates.put("EUR", 0.85);
        mExchangeRate.put("USD", usdExchangeRates);

        Map<String, Double> eurExchangeRates = new HashMap<>();
        eurExchangeRates.put("EUR", 1.0);
        eurExchangeRates.put("GBP", 0.85);
        eurExchangeRates.put("USD", 1.18);
        mExchangeRate.put("EUR", eurExchangeRates);

        Map<String, Double> gbpExchangeRates = new HashMap<>();
        gbpExchangeRates.put("GBP", 1.0);
        gbpExchangeRates.put("EUR", 1.18);
        gbpExchangeRates.put("USD", 1.38);
        mExchangeRate.put("GBP", gbpExchangeRates);
    }

    /**
     * This method returns the exchange rate between the {@code from}
     * and {@code to} parameters asynchronously.
     *
     * @param fromCurrency The 3 letter currency code to convert from.
     * @param toCurrency   The 3 letter currency code to convert to.
     * @return The exchange rate between the {@code from} and {@code
     * to} parameters.
     */
    public Double getRate(String fromCurrency, String toCurrency) {
        return mExchangeRate.get(fromCurrency).get(toCurrency);
    }

    /**
     * This method returns a Map that contains all known exchange rates
     * for {@code fromCurrency}.
     *
     * @param fromCurrency The 3 letter currency code to convert from
     * @return A {@link Map} that contains the all known exchange rates for
     * {@code fromCurrency}
     */
    public Map<String, Double> getRates(String fromCurrency) {
        return mExchangeRate.get(fromCurrency);
    }
}
