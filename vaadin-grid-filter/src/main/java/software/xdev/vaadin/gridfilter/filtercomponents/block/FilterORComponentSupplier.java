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
package software.xdev.vaadin.gridfilter.filtercomponents.block;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import software.xdev.vaadin.gridfilter.FilterableField;
import software.xdev.vaadin.gridfilter.business.operation.Operation;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.value.ValueContainer;
import software.xdev.vaadin.gridfilter.business.value.reuse.ValueReUseAdapter;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponent;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponentSupplier;


public class FilterORComponentSupplier implements FilterComponentSupplier
{
	@Override
	public String display()
	{
		return "OR block";
	}
	
	@Override
	public String serializationPrefix()
	{
		return "_OR";
	}
	
	@Override
	public <T> FilterComponent<T, ?> create(
		final List<FilterableField<T, ?>> filterableFields,
		final Function<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> fieldDataResolver,
		final Map<Class<? extends ValueContainer>, Set<ValueReUseAdapter<?>>> valueReUseAdapters,
		final List<FilterComponentSupplier> filterComponentSuppliers,
		final Runnable onValueUpdated)
	{
		return new FilterBlockComponent<>(
			filterableFields,
			fieldDataResolver,
			valueReUseAdapters,
			filterComponentSuppliers,
			onValueUpdated,
			Stream::anyMatch,
			"OR",
			this::serializationPrefix);
	}
}
