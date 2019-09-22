package com.finplant.cryptoharvester.cryptoharvester.services;

import com.finplant.cryptoharvester.cryptoharvester.entities.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SyntheticService {
    @Value("${synth_flush_period_ms}")
    private
    String synth_flush_periods_ms;

    private ScheduledExecutorService executorService;
    private Runnable runnable;

    @Autowired
    private
    Set<Quote> quoteSet;

    private Logger logger = LoggerFactory.getLogger(SyntheticService.class);

    /**
     * Initiates algorithm for generation synthetic
     * quotes
     */
    public SyntheticService() {
        executorService = Executors.newScheduledThreadPool(10);
        runnable = () -> {

            for (Quote quote : quoteSet) {
                if (quote.getDepends().size() > 1) {
                    String firstDependant = quote.getDepends().get(0);
                    String secondDependant = quote.getDepends().get(1);
                    Quote firstDependantQuote = getQuoteOfInstrument(firstDependant);
                    Quote secondDependantQuote = getQuoteOfInstrument(secondDependant);
                    if(
                            firstDependantQuote !=null && secondDependantQuote !=null
                                    && firstDependantQuote.getBid()!=null && secondDependantQuote.getBid()!=null
                                    && firstDependantQuote.getAsk()!=null && secondDependantQuote.getAsk()!=null) {

                        quote.setBid(firstDependantQuote.getBid().divide(secondDependantQuote.getBid(), RoundingMode.HALF_UP));
                        logger.info("Original Bids: " +firstDependantQuote.getBid() +"/"+secondDependantQuote.getBid());
                        quote.setAsk(firstDependantQuote.getAsk().divide(secondDependantQuote.getAsk(), RoundingMode.HALF_UP));
                        logger.info("Original asks: " + firstDependantQuote.getAsk()+"/"+secondDependantQuote.getAsk());
                        quote.setTime(Time.valueOf(LocalTime.now()));


                        logger.info("Synthesized :" + quote.getName());
                        logger.info("Bid :" + quote.getBid());
                        logger.info("Ask: " + quote.getAsk());
                    }
                }
            }

        };
    }

    /**
     * Starts synthetic loop generator
     */
    public void initSynthService() {
        executorService.scheduleAtFixedRate(runnable, 0, Integer.parseInt(synth_flush_periods_ms), TimeUnit.MILLISECONDS);
    }

    /**
     * Stops synthetic loop generator
     */
    public void stopSyntheticService(){
        executorService.shutdown();
    }

    /**
     * Searches for quote with provided instrument
     * @param instrument instrument provided, e.g. ETH/USDT
     * @return returns Quote with same exchange instrument if found, or returns null
     */
    public Quote getQuoteOfInstrument(String instrument) {
        for (Quote localQuote : quoteSet) {
            if (    localQuote.getExchange().equals(instrument)
                    && localQuote.getDepends().size() < 1)
                return localQuote;
        }
        return null;
    }
}
