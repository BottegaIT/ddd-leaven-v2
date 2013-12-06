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
import java.util.UUID;

import javax.inject.Inject;

import pl.com.bottega.cqrs.command.Gate;

// @ExternalApplicationService
public class ExampleServiceIfYouActuallyWantToUseCommands {

	@Inject
	private Gate gate;

	@Inject
	private FinderHelper finderHelper;

	/**
	 * @return id of the newly created object
	 */
	public UUID createSomething(String param1, List<Long> idsOfSomeSort) {
		UUID id = UUID.randomUUID();
		CreateSomethingCommand cmd = new CreateSomethingCommand(id, param1, idsOfSomeSort);
		gate.dispatch(cmd);
		// optionally - create an object with this ID so when the client asks for it it will get it with status CREATING
		// when the command finishes successfully this object will be overriden by the representation of the actual created object
		// if the command fails this object will be overriden with status "NOT CREATED" and the error message
		finderHelper.createObject(id, "Something", "{status: 'Creating'}");
		return id;
	}
}

class CreateSomethingCommand {
	private UUID somethingId;
	private String param1;
	private List<Long> idsOfSomeSort;

	public CreateSomethingCommand(UUID somethingId, String param1, List<Long> idsOfSomeSort) {
		this.somethingId = somethingId;
		this.param1 = param1;
		this.idsOfSomeSort = idsOfSomeSort;
	}
	
	public UUID getSomethingId() {
		return somethingId;
	}
	
	public String getParam1() {
		return param1;
	}
	
	public List<Long> getIdsOfSomeSort() {
		return idsOfSomeSort;
	}
}

interface FinderHelper {

	void createObject(UUID id, String type, String value);

}