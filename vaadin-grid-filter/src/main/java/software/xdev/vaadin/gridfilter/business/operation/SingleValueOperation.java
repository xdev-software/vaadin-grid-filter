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
package software.xdev.vaadin.gridfilter.business.operation;

import java.util.Objects;

import software.xdev.vaadin.gridfilter.business.value.SingleValue;


public abstract class SingleValueOperation<C> implements Operation<SingleValue<C>>
{
	protected final Class<C> supportedClass;
	protected final String display; // TODO: I18N System like Grid Exporter
	
	protected SingleValueOperation(final Class<C> supported, final String display)
	{
		this.supportedClass = Objects.requireNonNull(supported);
		this.display = Objects.requireNonNull(display);
	}
	
	@Override
	public Class<?> valueContainerClass()
	{
		return SingleValue.class;
	}
	
	@Override
	public boolean canHandle(final Class<?> clazz)
	{
		return this.supportedClass.isAssignableFrom(clazz);
	}
	
	@Override
	public String identifier()
	{
		return this.display;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean test(final Object input, final SingleValue<C> filterValue)
	{
		if(!this.supportedClass.isInstance(input))
		{
			return false;
		}
		return this.testTyped((C)input, filterValue);
	}
	
	public abstract boolean testTyped(final C input, final SingleValue<C> filterValue);
}
