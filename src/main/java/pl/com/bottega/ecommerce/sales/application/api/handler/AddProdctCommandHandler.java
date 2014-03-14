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
package pl.com.bottega.ecommerce.sales.application.api.handler;

import javax.inject.Inject;

import pl.com.bottega.cqrs.annotations.CommandHandlerAnnotation;
import pl.com.bottega.cqrs.command.handler.CommandHandler;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProdctCommand;
import pl.com.bottega.ecommerce.sales.domain.client.Client;
import pl.com.bottega.ecommerce.sales.domain.client.ClientRepository;
import pl.com.bottega.ecommerce.sales.domain.equivalent.SuggestionService;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.ProductRepository;
import pl.com.bottega.ecommerce.sales.domain.reservation.Reservation;
import pl.com.bottega.ecommerce.sales.domain.reservation.ReservationRepository;
import pl.com.bottega.ecommerce.system.application.SystemContext;

@CommandHandlerAnnotation
public class AddProdctCommandHandler implements CommandHandler<AddProdctCommand, Void>{

	@Inject
	private ReservationRepository reservationRepository;
	
	@Inject
	private ProductRepository productRepository;
	
	@Inject
	private SuggestionService suggestionService;
	
	@Inject
	private ClientRepository clientRepository;
	
	@Inject
	private SystemContext systemContext;
	
	@Override
	public Void handle(AddProdctCommand command) {
		Reservation reservation = reservationRepository.load(command.getOrderId());
		
		Product product = productRepository.load(command.getProductId());
		
		if (! product.isAvailabe()){
			Client client = loadClient();	
			product = suggestionService.suggestEquivalent(product, client);
		}
			
		reservation.add(product, command.getQuantity());
		
		reservationRepository.save(reservation);
		
		return null;
	}
	
	private Client loadClient() {
		return clientRepository.load(systemContext.getSystemUser().getClientId());
	}

}
