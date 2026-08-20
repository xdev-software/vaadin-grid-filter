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
package software.xdev.vaadin.gridfilter.business.typevaluecomp.single;

import java.util.function.Function;
import java.util.function.Supplier;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.binder.Binder;

import software.xdev.vaadin.gridfilter.business.value.SingleValue;


public class SingleValueNotRequiredComponentProvider<T, C extends Component & HasValue<?, T>>
	extends SingleValueComponentProvider<T, C>
{
	public SingleValueNotRequiredComponentProvider(
		final Class<T> clazz,
		final Supplier<C> componentSupplier,
		final Function<T, String> serializeFunc,
		final Function<String, T> deserializeFunc)
	{
		super(clazz, componentSupplier, serializeFunc, deserializeFunc);
	}
	
	@Override
	protected void handleBindingBuilder(final Binder.BindingBuilder<SingleValue<T>, T> bindingBuilder)
	{
		// No require is added
	}
}
