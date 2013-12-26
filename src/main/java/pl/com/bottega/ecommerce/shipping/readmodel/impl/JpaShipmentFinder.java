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
package pl.com.bottega.ecommerce.shipping.readmodel.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import pl.com.bottega.ddd.annotations.application.Finder;
import pl.com.bottega.ecommerce.shipping.readmodel.ShipmentDto;
import pl.com.bottega.ecommerce.shipping.readmodel.ShipmentFinder;

@Finder
public class JpaShipmentFinder implements ShipmentFinder {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
	@Override
    public List<ShipmentDto> findShipment() {
        String jpql = "select new pl.com.bottega.erp.shipping.presentation.ShipmentDto(s.id, s.orderId, s.status) from Shipment s";
        Query query = entityManager.createQuery(jpql);
        return query.getResultList();
    }
}
