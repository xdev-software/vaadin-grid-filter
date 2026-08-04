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

import java.util.Collections;
import java.util.Set;

import software.xdev.vaadin.gridfilter.business.value.ValueContainer;


public abstract class DefaultTypeValueComponentProvider<V extends ValueContainer>
	implements TypeValueComponentProvider<V>
{
	protected final Set<Class<?>> supportedTypes;
	
	protected DefaultTypeValueComponentProvider(final Set<Class<?>> supportedTypes)
	{
		this.supportedTypes = Collections.unmodifiableSet(supportedTypes);
	}
	
	@Override
	public Set<Class<?>> supportedTypes()
	{
		return this.supportedTypes;
	}
}
