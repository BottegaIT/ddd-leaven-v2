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
package pl.com.bottega.ecommerce.shipping.application.commands.handlers;

import javax.inject.Inject;

import pl.com.bottega.cqrs.annotations.CommandHandlerAnnotation;
import pl.com.bottega.cqrs.command.handler.CommandHandler;
import pl.com.bottega.ecommerce.shipping.application.commands.DeliverShipmentCommand;
import pl.com.bottega.ecommerce.shipping.domain.Shipment;
import pl.com.bottega.ecommerce.shipping.domain.ShipmentRepository;

@CommandHandlerAnnotation
public class DeliverShipmentCommandHandler implements CommandHandler<DeliverShipmentCommand, Void> {

    @Inject
    private ShipmentRepository repository;

    @Override
    public Void handle(DeliverShipmentCommand command) {
        Shipment shipment = repository.load(command.getShipmentId());
        shipment.deliver();
        return null;
    }
}
