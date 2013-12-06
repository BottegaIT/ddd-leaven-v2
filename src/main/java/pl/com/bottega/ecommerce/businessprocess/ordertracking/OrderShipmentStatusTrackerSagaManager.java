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
package pl.com.bottega.ecommerce.businessprocess.ordertracking;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import pl.com.bottega.ecommerce.canonicalmodel.events.OrderSubmittedEvent;
import pl.com.bottega.ecommerce.shipping.domain.events.OrderShippedEvent;
import pl.com.bottega.ecommerce.shipping.domain.events.ShipmentDeliveredEvent;
import pl.com.bottega.ecommerce.system.saga.SagaManager;
import pl.com.bottega.ecommerce.system.saga.annotations.LoadSaga;

@Component
public class OrderShipmentStatusTrackerSagaManager implements
        SagaManager<OrderShipmentStatusTrackerSaga, OrderShipmentStatusTrackerData> {

    @PersistenceContext
    private EntityManager entityManager;

    @LoadSaga
    public OrderShipmentStatusTrackerData loadSaga(OrderSubmittedEvent event) {
        return findByOrderId(event.getOrderId().getId());
    }

    @LoadSaga
    public OrderShipmentStatusTrackerData loadSaga(OrderShippedEvent event) {
        return findByOrderId(event.getOrderId().getId());
    }

    @LoadSaga
    public OrderShipmentStatusTrackerData loadSaga(ShipmentDeliveredEvent event) {
        return findByShipmentId(event.getShipmentId().getId());
    }

    private OrderShipmentStatusTrackerData findByOrderId(String orderId) {
        Query query = entityManager.createQuery("from OrderShipmentStatusTrackerData where orderId=:orderId")
                .setParameter("orderId", orderId);
        return (OrderShipmentStatusTrackerData) query.getSingleResult();
    }

    private OrderShipmentStatusTrackerData findByShipmentId(String shipmentId) {
        Query query = entityManager.createQuery("from OrderShipmentStatusTrackerData where shipmentId=:shipmentId")
                .setParameter("shipmentId", shipmentId);
        return (OrderShipmentStatusTrackerData) query.getSingleResult();
    }

    @Override
    public void removeSaga(OrderShipmentStatusTrackerSaga saga) {
        OrderShipmentStatusTrackerData sagaData = entityManager.merge(saga.getData());
        entityManager.remove(sagaData);
    }

    @Override
    public OrderShipmentStatusTrackerData createNewSagaData() {
        OrderShipmentStatusTrackerData sagaData = new OrderShipmentStatusTrackerData();
        entityManager.persist(sagaData);
        return sagaData;
    }
}
