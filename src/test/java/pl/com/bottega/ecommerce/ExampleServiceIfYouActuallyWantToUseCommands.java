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
package pl.com.bottega.ecommerce;

import java.util.List;

import javax.inject.Inject;

import pl.com.bottega.cqrs.command.Gate;
import pl.com.bottega.ecommerce.sales.application.api.command.AddProdctCommand;

// @ExternalApplicationService
public class ExampleServiceIfYouActuallyWantToUseCommands {

	@Inject
	private Gate gate;

	public void createSomething(String param1, List<Long> idsOfSomeSort) {
		AddProdctCommand cmd = new AddProdctCommand(null, null, 0);
		
		gate.dispatch(cmd);
	}
}