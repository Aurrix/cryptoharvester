package com.finplant.cryptoharvester.cryptoharvester;

import com.finplant.cryptoharvester.cryptoharvester.config.ErrorLogging;
import com.finplant.cryptoharvester.cryptoharvester.entities.Quote;
import com.finplant.cryptoharvester.cryptoharvester.services.ExchangeService;
import com.finplant.cryptoharvester.cryptoharvester.services.SyntheticService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.LinkedHashSet;
import java.util.Set;

@SpringBootApplication
public class CryptoHarvesterApplication {
    /**
    Global Buffer of quotes.
     Singleton type bean
     */
	Set<Quote> quotesSet = new LinkedHashSet<>();
    @Bean
    public Set<Quote> getQuoteSet() {
        return quotesSet;
    }

    public static void main(String[] args){
        ApplicationContext context = SpringApplication.run(CryptoHarvesterApplication.class, args);

        //Initiating exchange service loop
        ExchangeService loop = context.getBean(ExchangeService.class);
        loop.loadQuotesFromInstruments();
        loop.init();
        loop.initXChangeTicker();

        //Initiating synthetic exchange loop
		SyntheticService syntheticService = context.getBean(SyntheticService.class);
		syntheticService.initSynthService();

        //Declaring global exception handler
        ErrorLogging errorLogging = context.getBean(ErrorLogging.class);
        Thread.setDefaultUncaughtExceptionHandler(errorLogging);

    }

}
