package ru.storage.server.controller.services.format.currency;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

// TODO: Debug.
public final class CurrencyCalculator extends CurrencyFormat {
  private final NumberFormat numberFormat;

  CurrencyCalculator(Locale locale) {
    numberFormat = NumberFormat.getCurrencyInstance(locale);
  }

  @Override
  public String format(double value) {
    try {
      Stock stock = YahooFinance.get(numberFormat.getCurrency().getCurrencyCode());
      double price = stock.getQuote().getPrice().doubleValue();
      value *= price;
      return numberFormat.format(value);
    } catch (IOException e) {
      return NumberFormat.getCurrencyInstance(Locale.US).format(value);
    }
  }
}
