/*
 * Copyright © 2024 XDEV Software (https://xdev.software)
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
package software.xdev.vaadin.gridfilter.business.typevaluecomp;

import java.util.Set;

import software.xdev.vaadin.gridfilter.business.value.ValueContainer;


public interface TypeValueComponentProvider<V extends ValueContainer>
{
	Class<?> valueContainerClass();
	
	V createEmptyValueContainer();
	
	default Set<Class<?>> supportedTypes()
	{
		return Set.of();
	}
	
	default boolean canHandle(final Class<?> clazz)
	{
		return this.supportedTypes().stream().anyMatch(supported -> supported.isAssignableFrom(clazz));
	}
	
	TypeValueComponentData<V> getNewComponentData(final Class<?> clazz);
	
	default TypeValueComponentData<V> getNewComponentDataWithDefaults(final Class<?> clazz)
	{
		final TypeValueComponentData<V> componentData = this.getNewComponentData(clazz);
		componentData.binder().setBean(this.createEmptyValueContainer());
		return componentData;
	}
	
	String serialize(TypeValueComponentData<V> typeValueComponentData);
	
	@SuppressWarnings("unchecked")
	default String serializeUnchecked(final TypeValueComponentData<?> typeValueComponentData)
	{
		return this.serialize((TypeValueComponentData<V>)typeValueComponentData);
	}
	
	void deserializeAndApply(String input, TypeValueComponentData<V> typeValueComponentData);
	
	@SuppressWarnings("unchecked")
	default void deserializeAndApplyUnchecked(
		final String input,
		final TypeValueComponentData<?> typeValueComponentData)
	{
		this.deserializeAndApply(input, (TypeValueComponentData<V>)typeValueComponentData);
	}
}
