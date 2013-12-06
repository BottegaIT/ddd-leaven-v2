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
package pl.com.bottega.ecommerce.sales.domain.productscatalog;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import pl.com.bottega.ddd.annotations.domain.ValueObject;
import pl.com.bottega.ecommerce.canonicalmodel.publishedlanguage.AggregateId;
import pl.com.bottega.ecommerce.sharedkernel.Money;

@Embeddable
@ValueObject
public class ProductData {

	@Embedded
	private AggregateId productId;
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "denomination", column = @Column(name = "productPrice_denomination")),
		@AttributeOverride(name = "currencyCode", column = @Column(name = "productPrice_currencyCode")) })
	private Money price;
	
	private String name;
	
	private Date snapshotDate;
	
	@Enumerated(EnumType.STRING)
	private ProductType type;

	
	@SuppressWarnings("unused")
	private ProductData(){}
	
	ProductData(AggregateId productId, Money price, String name, ProductType type, 
			Date snapshotDate) {
		this.productId = productId;
		this.price = price;
		this.name = name;
		this.snapshotDate = snapshotDate;
		this.type = type;
	}

	public AggregateId getProductId() {
		return productId;
	}

	public Money getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}

	public Date getSnapshotDate() {
		return snapshotDate;
	}
	
	public ProductType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductData other = (ProductData) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	
}
