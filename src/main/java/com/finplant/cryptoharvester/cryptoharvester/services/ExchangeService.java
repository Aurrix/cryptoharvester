package com.finplant.cryptoharvester.cryptoharvester.services;

import com.finplant.cryptoharvester.cryptoharvester.config.Instrument;
import com.finplant.cryptoharvester.cryptoharvester.config.Instruments;
import com.finplant.cryptoharvester.cryptoharvester.entities.Quote;
import com.finplant.cryptoharvester.cryptoharvester.repositories.QuoteRepository;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.poloniex2.PoloniexStreamingExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ExchangeService {
    @Value("${flush_periods_s}")
    private
    String flush_periods_s;

    @Autowired
    private
    Instruments instruments;

    @Autowired
    private
    QuoteRepository quoteRepository;

    @Autowired
    private
    Set<Quote> quoteSet;

    private Runnable runnable;
    private ScheduledExecutorService executorService;


    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private StreamingExchange exchange;
    private Set<CurrencyPair> currencyPairSet = new HashSet<>();

    /**
     * Initiates persistence to database
     */
    public ExchangeService() {
        executorService = Executors.newScheduledThreadPool(10);
        runnable = () -> quoteRepository.saveAll(quoteSet);
    }

    /**
     * Connects to exchange services and updates
     * received values into the buffer
     */
     public synchronized void initXChangeTicker() {
        for (Quote quote : quoteSet) {
            exchange.getStreamingMarketDataService().getTicker(
                    new CurrencyPair(quote.getExchange())).subscribe(ticker -> {
                        if(quote.getDepends()!=null){
                            LOG.info("TICKER: {}", ticker);
                            LOG.info(String.valueOf(ticker.getAsk()));
                            LOG.info(String.valueOf(ticker.getBid()));
                            quote.setAsk(ticker.getAsk());
                            quote.setBid(ticker.getBid());
                            quote.setTime(Time.valueOf(LocalTime.now()));
                        }
            }, throwable -> LOG.error("ERROR in getting ticker: ", throwable));

        }
    }

    /**
     * Loads quotes from yaml generated instruments
     * to the buffer
     */
    public void loadQuotesFromInstruments() {
        for (Instrument instrument : instruments.getInstruments()) {
            Quote localQuote = new Quote();
            localQuote.setName(instrument.getName());
            localQuote.setExchange(instrument.getInstrument());
            if (instrument.getDepends().size()>1) localQuote.setDepends(new ArrayList<>(instrument.getDepends()));
            quoteSet.add(localQuote);
        }
    }

    /**
     * Starts persistence loop and exchange ticker
     * flush_periods_s - seconds each persistence operation made
     */
    public void init() {
        exchange = StreamingExchangeFactory.INSTANCE.createExchange(PoloniexStreamingExchange.class.getName());
        exchange.connect().blockingAwait();
        executorService.scheduleAtFixedRate(runnable, 0, Integer.parseInt(flush_periods_s), TimeUnit.SECONDS);
    }

    /**
     * Stops exchange and persistence loops
     */
    public void stopExchangeService(){
        executorService.shutdown();
        exchange.disconnect();
    }

}
