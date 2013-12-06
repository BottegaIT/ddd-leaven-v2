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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.AggregateId;

@Entity
public class OrderShipmentStatusTrackerData {

    @Id
    @GeneratedValue
    private Long id;

    @AttributeOverrides({
		@AttributeOverride(name = "aggregateId", column = @Column(name = "orderId"))})
    private AggregateId orderId;

    @AttributeOverrides({
		@AttributeOverride(name = "aggregateId", column = @Column(name = "shipmentId"))})
    private AggregateId shipmentId;

    private Boolean shipmentReceived = false;

    public AggregateId getOrderId() {
        return orderId;
    }

    /**
     * Id of order aggregate.
     */
    public void setOrderId(AggregateId orderId) {
        this.orderId = orderId;
    }

    /**
     * Id of shipment aggregate (from shipment module) once it's created.
     */
    public AggregateId getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(AggregateId shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Boolean getShipmentReceived() {
        return shipmentReceived;
    }

    public void setShipmentReceived(Boolean shipmentReceived) {
        this.shipmentReceived = shipmentReceived;
    }
}
