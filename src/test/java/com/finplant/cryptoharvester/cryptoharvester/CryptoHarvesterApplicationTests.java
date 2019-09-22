package com.finplant.cryptoharvester.cryptoharvester;

import com.finplant.cryptoharvester.cryptoharvester.entities.Quote;
import com.finplant.cryptoharvester.cryptoharvester.services.ExchangeService;
import com.finplant.cryptoharvester.cryptoharvester.services.SyntheticService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CryptoHarvesterApplicationTests {

	@Autowired
	SyntheticService syntheticService;

	@Autowired
	ExchangeService exchangeService;

	@Autowired
	Set<Quote> quotes;

	@Test
	public void loadQuotesTest() {
		Assert.assertTrue(quotes.size()<1);
		exchangeService.loadQuotesFromInstruments();
		Assert.assertTrue(quotes.size()>1);
	}

	//@Test
	public void exchangeServiceTest() {
		exchangeService.loadQuotesFromInstruments();
		exchangeService.init();
		exchangeService.initXChangeTicker();

		for(Quote localQuote:quotes){
			if(localQuote.getDepends()==null){
				while (localQuote.getAsk()==null && localQuote.getBid()==null){
				//wait till quotes received from exchange
				}
				Assert.assertNotNull(localQuote.getBid());
				Assert.assertNotNull(localQuote.getAsk());
			}
		}
	}

	@Test
	public void getQuoteOfInstrumentTest(){
		exchangeService.loadQuotesFromInstruments();
		Quote quote = syntheticService.getQuoteOfInstrument("BTC/USDT");
		Assert.assertEquals(quote.getName(),"BTCUSD");
	}

}
