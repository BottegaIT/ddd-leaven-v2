package pl.com.bottega.ecommerce.sales.domain.equivalent.specification;

import pl.com.bottega.ecommerce.sales.domain.productscatalog.Product;
import pl.com.bottega.ecommerce.sharedkernel.specification.CompositeSpecification;

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
public class SimilarName extends CompositeSpecification<Product>{

	private String name;
	
	public SimilarName(String name) {
		this.name = name;
	}

	@Override
	public boolean isSatisfiedBy(Product candidate) {		
		return candidate.getName().contains(name) || candidate.getProductType().toString().contains(name);
	}

}
