/*
 * Copyright 2011-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.com.bottega.ecommerce.sales.acceptancetests;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.AggregateId;
import pl.com.bottega.ecommerce.sales.application.api.command.OrderDetailsCommand;
import pl.com.bottega.ecommerce.sales.application.api.service.OfferChangedExcpetion;
import pl.com.bottega.ecommerce.sales.application.api.service.OrderingService;
import pl.com.bottega.ecommerce.sales.domain.offer.Offer;
import pl.com.bottega.ecommerce.sales.readmodel.OrderFinder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/functionalTestsContext.xml")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class OrderingTest {

	@Inject
	OrderingService orderingService;

	@Inject
	OrderFinder orderFinder;

	@Inject
	AuthenticationTestHelper authenticationHelper;

	@Before
	public void givenImAuthenticated() throws Exception {
		authenticationHelper.asBuyer();
	}

	@After
	public void tearDown() throws Exception {
		authenticationHelper.deauthenticate();
	}
	
	@Test
	public void shouldPurchaseProducts(){
		AggregateId orderId = orderingService.createOrder();
		orderingService.addProduct(orderId, new AggregateId("p1"), 1);
		orderingService.addProduct(orderId, new AggregateId("p2"), 20);
		Offer offer = orderingService.calculateOffer(orderId);
		
		orderingService.confirm(orderId, new OrderDetailsCommand(), offer);
	}
	
	@Test
	public void canNotPurchaseIfOfferChanged(){
		AggregateId orderId = orderingService.createOrder();
		orderingService.addProduct(orderId, new AggregateId("p1"), 1);
		orderingService.addProduct(orderId, new AggregateId("p2"), 20);
		
		Offer offer = orderingService.calculateOffer(orderId);
		//change order 
		orderingService.addProduct(orderId, new AggregateId("p2"), 30);
		//confirm obsolete offer
		try{
			orderingService.confirm(orderId, new OrderDetailsCommand(), offer);
			Assert.fail();
		}
		catch(OfferChangedExcpetion e){}
	}
}
