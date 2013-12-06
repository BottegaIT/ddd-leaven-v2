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
/**
 * 
 */
package pl.com.bottega.cqrs.command.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import pl.com.bottega.cqrs.annotations.Command;

/**
 * Manages command execution history based on {@link Command} annotation attributes<br>
 * Commands that are annotated as unique=true are stored in this history.<br>
 * History checks if the same command (equals) is called again.<br>
 * <br>
 * Each command class has it's own entries in history - history length can be parameterized via constructor parameter. 
 * 
 * @author Slawek
 * 
 */
class GateHistory {

	@SuppressWarnings("serial")
	// TODO Sprawdzic czy nie musi byc concurrent (history jest, na tym
	// poziomie nie musi byc totalnej synchronizacji, to tylko rodzaj
	// cache)
	private class CommandExecutionsMap extends LinkedHashMap<Object, Date> {
		protected boolean removeEldestEntry(Map.Entry<Object, Date> eldest) {
			return this.size() > maxHistoryCapacity;
		}
	};

	private static final int DEFAULT_MAX_HISTORY_CAPACITY = 3;

	/**
	 * History model. Each command class has map of executions (command instance
	 * and time)
	 */
	@SuppressWarnings("rawtypes")
	private Map<Class, CommandExecutionsMap> history = new ConcurrentHashMap<Class, CommandExecutionsMap>();

	private int maxHistoryCapacity;

	public GateHistory(int maxHistoryCapacity) {
		this.maxHistoryCapacity = maxHistoryCapacity;
	}

	public GateHistory() {
		this(DEFAULT_MAX_HISTORY_CAPACITY);
	}

	/**
	 * 
	 * @param command
	 * @return true if command is not a repetition, false if command is
	 *         repetition and should not be executed now
	 */
	public boolean register(Object command) {
		if (!isUnique(command))
			return true;

		Date lastRun = getFromHistory(command);

		// update history
		Date now = new Date();
		addToHistory(command, now);

		// first run, so go
		if (lastRun == null)
			return true;

		long uniqueStorageTimeout = getUniqueStorageTimeout(command);
		// no timeout so by default it is duplicated
		if (uniqueStorageTimeout == 0)
			return false;

		long milisFromLastRun = now.getTime() - lastRun.getTime();
		return milisFromLastRun > uniqueStorageTimeout;
	}

	private boolean isUnique(Object command) {
		if (!command.getClass().isAnnotationPresent(Command.class))
			return false;

		Command commandAnnotation = command.getClass().getAnnotation(
				Command.class);

		return commandAnnotation.unique();
	}

	private Long getUniqueStorageTimeout(Object command) {
		Command commandAnnotation = command.getClass().getAnnotation(
				Command.class);
		return commandAnnotation.uniqueStorageTimeout();
	}

	private Date getFromHistory(Object command) {
		Map<Object, Date> executions = history.get(command.getClass());
		if (executions == null)
			return null;
		return executions.get(command);
	}

	private void addToHistory(Object command, Date executeDate) {
		CommandExecutionsMap executions = history.get(command.getClass());
		if (executions == null) {
			executions = new CommandExecutionsMap();
			history.put(command.getClass(), executions);
		}
		executions.put(command, executeDate);
	}
}
