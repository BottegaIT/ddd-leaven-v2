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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import pl.com.bottega.ddd.annotations.domain.AggregateRoot;
import pl.com.bottega.ddd.annotations.domain.Function;
import pl.com.bottega.ddd.annotations.domain.Invariant;
import pl.com.bottega.ddd.annotations.domain.InvariantsList;
import pl.com.bottega.ddd.support.domain.BaseAggregateRoot;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.AggregateId;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.ClientData;
import pl.com.bottega.ecommerce.sales.domain.offer.Discount;
import pl.com.bottega.ecommerce.sales.domain.offer.DiscountPolicy;
import pl.com.bottega.ecommerce.sales.domain.offer.Offer;
import pl.com.bottega.ecommerce.sales.domain.offer.OfferItem;
import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sharedkernel.Money;

/**
 * Reservation is just a "wish list". System can not guarantee that user can buy desired products.</br>
 * Reservation generates Offer VO, that is calculated based on current prices and current avability.
 * 
 * @author Slawek
 *
 */

@InvariantsList({
	"closed: closed reservation cano not be modified",
	"duplicates: can not add already added product, increase quantity instead",
})

@Entity
@AggregateRoot
public class Reservation extends BaseAggregateRoot{
	
	public enum ReservationStatus{
		OPENED, CLOSED
	}
	
	@Enumerated(EnumType.STRING)
	private ReservationStatus status;

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "reservation")
	@Fetch(FetchMode.JOIN)
	private List<ReservationItem> items;

	@Embedded
	private ClientData clientData;

	private Date createDate;

	@SuppressWarnings("unused")
	private Reservation() {}

	Reservation(AggregateId aggregateId, ReservationStatus status, ClientData clientData, Date createDate){
		this.aggregateId = aggregateId;
		this.status = status;
		this.clientData = clientData;
		this.createDate = createDate;
		this.items = new ArrayList<ReservationItem>();
	}

	@Invariant({"closed", "duplicates"})
	public void add(Product product, int quantity){
		if (isClosed())
			domainError("Reservation already closed");
		if (!product.isAvailabe())
			domainError("Product is no longer available");
		
		if (contains(product)){
			increase(product, quantity);			
		}
		else{
			addNew(product, quantity);
		}
	}
	
	/**
	 * Sample function closured by policy </br> 
	 * Higher order function closured by policy function</br>
	 * </br>
	 * Function loads current prices, and prepares offer according to the current availability and given discount
	 * @param discountPolicy
	 * @return
	 */
	@Function
	public Offer calculateOffer(DiscountPolicy discountPolicy) {
		List<OfferItem> availabeItems = new ArrayList<OfferItem>();
		List<OfferItem> unavailableItems = new ArrayList<OfferItem>();
		
		for (ReservationItem item : items) {						
			if (item.getProduct().isAvailabe()){
				Discount discount = discountPolicy.applyDiscount(item.getProduct(), item.getQuantity(), item.getProduct().getPrice());
				OfferItem offerItem = new OfferItem(item.getProduct().generateSnapshot(), item.getQuantity(), discount);
				
				availabeItems.add(offerItem);
			}
			else {
				OfferItem offerItem = new OfferItem(item.getProduct().generateSnapshot(), item.getQuantity());
				
				unavailableItems.add(offerItem);
			}
		}
		
		return new Offer(availabeItems, unavailableItems);
	}

	private void addNew(Product product, int quantity) {
		ReservationItem item = new ReservationItem(product, quantity);
		items.add(item);
	}

	private void increase(Product product, int quantity) {
		for (ReservationItem item : items) {
			if (item.getProduct().equals(product)){
				item.changeQuantityBy(quantity);
				break;
			}
		}
	}

	public boolean contains(Product product) {
		for (ReservationItem item : items) {
			if (item.getProduct().equals(product))
				return true;
		}
		return false;
	}

	public boolean isClosed() {
		return status.equals(ReservationStatus.CLOSED);
	}
	
	@Invariant({"closed"})
	public void close(){
		if (isClosed())
			domainError("Reservation is already closed");
		status = ReservationStatus.CLOSED;
	}

	public List<ReservedProduct> getReservedProducts() {
		ArrayList<ReservedProduct> result = new ArrayList<ReservedProduct>(items.size());
		
		for (ReservationItem item : items) {
			result.add(new ReservedProduct(item.getProduct().getAggregateId(), item.getProduct().getName(), item.getQuantity(), calculateItemCost(item)));
		}
		
		return result;
	}
	
	private Money calculateItemCost(ReservationItem item){
		return item.getProduct().getPrice().multiplyBy(item.getQuantity());
	}

	

	public ClientData getClientData() {
		return clientData;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public ReservationStatus getStatus() {
		return status;
	}
}
