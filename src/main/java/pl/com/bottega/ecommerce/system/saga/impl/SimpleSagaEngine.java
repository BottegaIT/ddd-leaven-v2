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
package pl.com.bottega.ecommerce.system.saga.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Component;

import pl.com.bottega.ecommerce.system.infrastructure.events.SimpleEventPublisher;
import pl.com.bottega.ecommerce.system.infrastructure.events.impl.handlers.EventHandler;
import pl.com.bottega.ecommerce.system.saga.SagaEngine;
import pl.com.bottega.ecommerce.system.saga.SagaInstance;
import pl.com.bottega.ecommerce.system.saga.SagaManager;
import pl.com.bottega.ecommerce.system.saga.annotations.LoadSaga;
import pl.com.bottega.ecommerce.system.saga.annotations.SagaAction;

/**
 * @author Rafał Jamróz
 */
@Component
public class SimpleSagaEngine implements SagaEngine {

    private final SagaRegistry sagaRegistry;

    private final SimpleEventPublisher eventPublisher;

    @Inject
    public SimpleSagaEngine(SagaRegistry sagaRegistry, SimpleEventPublisher eventPublisher) {
        this.sagaRegistry = sagaRegistry;
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    public void registerEventHandler() {
        eventPublisher.registerEventHandler(new SagaEventHandler(this));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void handleSagasEvent(Object event) {
        Collection<SagaManager> loaders = sagaRegistry.getLoadersForEvent(event);
        for (SagaManager loader : loaders) {
            SagaInstance sagaInstance = loadSaga(loader, event);
            invokeSagaActionForEvent(sagaInstance, event);
            if (sagaInstance.isCompleted()) {
                loader.removeSaga(sagaInstance);
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private SagaInstance loadSaga(SagaManager loader, Object event) {
        Class<? extends SagaInstance> sagaType = determineSagaTypeByLoader(loader);
        Object sagaData = loadSagaData(loader, event);
        if (sagaData == null) {
            sagaData = loader.createNewSagaData();
        }
        SagaInstance sagaInstance = sagaRegistry.createSagaInstance(sagaType);
        sagaInstance.setData(sagaData);
        return sagaInstance;
    }

    // TODO determine saga type more reliably
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private Class<? extends SagaInstance> determineSagaTypeByLoader(SagaManager loader) {
        Type type = ((ParameterizedType) loader.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        return ((Class<? extends SagaInstance>) type);
    }

    /**
     * TODO handle exception in more generic way
     */
    @SuppressWarnings("rawtypes")
	private Object loadSagaData(SagaManager loader, Object event) {
        Method loaderMethod = findHandlerMethodForEvent(loader.getClass(), event);
        try {
            Object sagaData = loaderMethod.invoke(loader, event);
            return sagaData;
        } catch (InvocationTargetException e) {
            // NRE is ok here, it means that saga hasn't been started yet
            if (e.getTargetException() instanceof NoResultException) {
                return null;
            } else {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeSagaActionForEvent(SagaInstance<?> saga, Object event) {
        Method eventHandler = findHandlerMethodForEvent(saga.getClass(), event);
        try {
            eventHandler.invoke(saga, event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method findHandlerMethodForEvent(Class<?> type, Object event) {
        for (Method method : type.getMethods()) {
            if (method.getAnnotation(SagaAction.class) != null || method.getAnnotation(LoadSaga.class) != null) {
                if (method.getParameterTypes().length == 1
                        && method.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
                    return method;
                }
            }
        }
        throw new RuntimeException("no method handling " + event.getClass());
    }

    private static class SagaEventHandler implements EventHandler {

        private final SagaEngine sagaEngine;

        public SagaEventHandler(SagaEngine sagaEngine) {
            this.sagaEngine = sagaEngine;
        }

        @Override
        public boolean canHandle(Object event) {
            return true;
        }

        @Override
        public void handle(Object event) {
            sagaEngine.handleSagasEvent(event);
        }
    }
}
