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
package software.xdev.vaadin.gridfilter.business.typevaluecomp.novalue;

import java.util.Set;

import com.vaadin.flow.data.binder.Binder;

import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentData;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.value.NoValue;


public class NoValueComponentProvider implements TypeValueComponentProvider<NoValue>
{
	@Override
	public Class<?> valueContainerClass()
	{
		return NoValue.class;
	}
	
	@Override
	public NoValue createEmptyValueContainer()
	{
		return new NoValue();
	}
	
	@Override
	public Set<Class<?>> supportedTypes()
	{
		return Set.of(Object.class);
	}
	
	@Override
	public boolean canHandle(final Class<?> clazz)
	{
		return true;
	}
	
	@Override
	public TypeValueComponentData<NoValue> getNewComponentData(final Class<?> clazz)
	{
		return new TypeValueComponentData<>(new Binder<>(), null);
	}
	
	@Override
	public String serialize(final TypeValueComponentData<NoValue> typeValueComponentData)
	{
		return null;
	}
	
	@Override
	public void deserializeAndApply(final String input, final TypeValueComponentData<NoValue> typeValueComponentData)
	{
		// Nothing to do
	}
}
