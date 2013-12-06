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
package pl.com.bottega.ecommerce.sales.domain.reservation;

import org.junit.Assert;
import org.junit.Test;

import pl.com.bottega.ddd.annotations.domain.Invariant;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductObjectMother;

public class ReservationTest {

	@Invariant("duplicates")
	@Test
	public void shouldIncreaseQuantityWhenAddingAlreadyAddedProduct(){
		//given
		Reservation reservation = ReservationObjectMother.emptyReservation();
		Product product = ProductObjectMother.someProduct();
		reservation.add(product, 1);
		//when
		reservation.add(product, 1);
		//then
		Assert.assertEquals(1, reservation.getReservedProducts().size());
		Assert.assertEquals(2, reservation.getReservedProducts().get(0).getQuantity());
	}

	
}
