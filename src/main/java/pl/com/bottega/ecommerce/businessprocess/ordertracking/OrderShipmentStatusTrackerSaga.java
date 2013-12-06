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

import javax.inject.Inject;

import pl.com.bottega.ecommerce.canonicalmodel.events.OrderSubmittedEvent;
import pl.com.bottega.ecommerce.sales.readmodel.OrderFinder;
import pl.com.bottega.ecommerce.shipping.domain.events.OrderShippedEvent;
import pl.com.bottega.ecommerce.shipping.domain.events.ShipmentDeliveredEvent;
import pl.com.bottega.ecommerce.system.saga.SagaInstance;
import pl.com.bottega.ecommerce.system.saga.annotations.LoadSaga;
import pl.com.bottega.ecommerce.system.saga.annotations.Saga;
import pl.com.bottega.ecommerce.system.saga.annotations.SagaAction;

@Saga
public class OrderShipmentStatusTrackerSaga extends SagaInstance<OrderShipmentStatusTrackerData> {

    @Inject
    private OrderFinder orderFinder;

    @SagaAction
    public void handleOrderCreated(OrderSubmittedEvent event) {
        data.setOrderId(event.getOrderId());
        completeIfPossible();
    }

    @SagaAction
    public void orderShipped(OrderShippedEvent event) {
        data.setOrderId(event.getOrderId());
        data.setShipmentId(event.getShipmentId());
        completeIfPossible();
    }
    
    @SagaAction
    public void shipmentDelivered(ShipmentDeliveredEvent event) {
        data.setShipmentId(event.getShipmentId());
        data.setShipmentReceived(true);
        completeIfPossible();
    }

    private void completeIfPossible() {
        if (data.getOrderId() != null && data.getShipmentId() != null && data.getShipmentReceived()) {
            //TODO move process forward, ex call service or publish event
        	
            markAsCompleted();
        }
    }
}
