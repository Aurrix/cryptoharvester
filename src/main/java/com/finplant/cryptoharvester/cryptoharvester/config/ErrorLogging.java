package com.finplant.cryptoharvester.cryptoharvester.config;

import com.finplant.cryptoharvester.cryptoharvester.services.ExchangeService;
import com.finplant.cryptoharvester.cryptoharvester.services.SyntheticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorLogging implements Thread.UncaughtExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private
    SyntheticService syntheticService;

    @Autowired
    private
    ExchangeService exchangeService;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("Critical Error shutting down :"+e.getLocalizedMessage());
        logger.error(e.getLocalizedMessage());
        syntheticService.stopSyntheticService();
        exchangeService.stopExchangeService();
        System.exit(0);
    }
}
