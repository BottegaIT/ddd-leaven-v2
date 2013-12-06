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
package pl.com.bottega.ecommerce.sales.acceptancetests;

import javax.inject.Inject;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import pl.com.bottega.ecommerce.system.application.SystemUser;

@Component
@Profile("test")
public class AuthenticationTestHelper {

	@Inject
	private SystemUser systemUser;

	public void asBuyer() {
		
	}

	public void asSeller() {
	}

	// TODO chagne to rule and run it after each test
	public void deauthenticate() {
	}

}
