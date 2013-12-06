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
package pl.com.bottega.ecommerce.shipping.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;

import pl.com.bottega.ddd.annotations.domain.AggregateRoot;
import pl.com.bottega.ddd.support.domain.BaseAggregateRoot;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.AggregateId;
import pl.com.bottega.ecommerce.shipping.domain.events.OrderShippedEvent;
import pl.com.bottega.ecommerce.shipping.domain.events.ShipmentDeliveredEvent;

/**
 * @author Rafał Jamróz
 */
@Entity
@AggregateRoot
public class Shipment extends BaseAggregateRoot {

	@AttributeOverrides({
		@AttributeOverride(name = "aggregateId", column = @Column(name = "orderId"))})
    private AggregateId orderId;

    private ShippingStatus status;

    
    @SuppressWarnings("unused")
	private Shipment() {}

    Shipment(AggregateId shipmentId, AggregateId orderId) {
        this.aggregateId = shipmentId;
    	this.orderId = orderId;
        this.status = ShippingStatus.WAITING;
    }

    /**
     * Shipment has been sent to the customer.
     */
    public void ship() {
        if (status != ShippingStatus.WAITING) {
            throw new IllegalStateException("cannot ship in status " + status);
        }
        status = ShippingStatus.SENT;
        eventPublisher.publish(new OrderShippedEvent(orderId, getAggregateId()));
    }

    /**
     * Shipment has been confirmed received by the customer.
     */
    public void deliver() {
        if (status != ShippingStatus.SENT) {
            throw new IllegalStateException("cannot deliver in status " + status);
        }
        status = ShippingStatus.DELIVERED;
        eventPublisher.publish(new ShipmentDeliveredEvent(getAggregateId()));
    }

    public AggregateId getOrderId() {
        return orderId;
    }

}
