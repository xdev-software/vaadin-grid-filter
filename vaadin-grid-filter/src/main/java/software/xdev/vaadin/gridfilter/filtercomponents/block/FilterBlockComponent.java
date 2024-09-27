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
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import software.xdev.vaadin.gridfilter.AddFilterComponentsButtons;
import software.xdev.vaadin.gridfilter.FilterContainerComponent;
import software.xdev.vaadin.gridfilter.FilterableField;
import software.xdev.vaadin.gridfilter.business.operation.Operation;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.value.ValueContainer;
import software.xdev.vaadin.gridfilter.business.value.reuse.ValueReUseAdapter;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterBlockComponentSerialization;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponent;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponentSerialization;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponentSupplier;


@SuppressWarnings("java:S1948")
public class FilterBlockComponent<T>
	extends FilterComponent<T, HorizontalLayout>
	implements FilterComponentSerialization
{
	protected final List<FilterableField<T, ?>> filterableFields;
	protected final Function<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> fieldDataResolver;
	protected final Map<Class<? extends ValueContainer>, Set<ValueReUseAdapter<?>>> valueReUseAdapters;
	protected final List<FilterComponentSupplier> filterComponentSuppliers;
	protected final Runnable onValueUpdated;
	protected final Supplier<String> serializationPrefixSupplier;
	
	protected final BiPredicate<Stream<FilterComponent<T, ?>>, Predicate<FilterComponent<T, ?>>> testAggregate;
	
	protected final FilterContainerComponent<T> filterContainerComponent;
	protected final AddFilterComponentsButtons addFilterComponentButtons = new AddFilterComponentsButtons();
	
	public FilterBlockComponent(
		final List<FilterableField<T, ?>> filterableFields,
		final Function<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> fieldDataResolver,
		final Map<Class<? extends ValueContainer>, Set<ValueReUseAdapter<?>>> valueReUseAdapters,
		final List<FilterComponentSupplier> filterComponentSuppliers,
		final Runnable onValueUpdated,
		final BiPredicate<Stream<FilterComponent<T, ?>>, Predicate<FilterComponent<T, ?>>> testAggregate,
		final String identifierName,
		final Supplier<String> serializationPrefixSupplier)
	{
		this.filterableFields = filterableFields;
		this.fieldDataResolver = fieldDataResolver;
		this.valueReUseAdapters = valueReUseAdapters;
		this.filterComponentSuppliers = filterComponentSuppliers;
		this.onValueUpdated = onValueUpdated;
		this.testAggregate = testAggregate;
		this.serializationPrefixSupplier = serializationPrefixSupplier;
		
		final Span spBlockIdentifier = new Span(identifierName);
		spBlockIdentifier.setMinWidth("2.4em");
		spBlockIdentifier.getStyle().set("overflow-wrap", "anywhere");
		
		this.filterContainerComponent = new FilterContainerComponent<>(onValueUpdated, true);
		
		final VerticalLayout vlMainContainer = new VerticalLayout();
		vlMainContainer.setPadding(false);
		vlMainContainer.setSpacing(false);
		vlMainContainer.add(this.filterContainerComponent, this.addFilterComponentButtons);
		
		this.getContent().add(spBlockIdentifier, vlMainContainer);
		this.getContent().setAlignSelf(FlexComponent.Alignment.CENTER, spBlockIdentifier);
		
		this.getContent().setPadding(false);
		this.getContent().setSpacing(false);
		this.getStyle().set("background-color", "var(--lumo-contrast-5pct)");
		this.getStyle().set("padding-left", "var(--lumo-space-xs)");
		this.getStyle().set("padding-right", "var(--lumo-space-xs)");
	}
	
	protected FilterComponent<T, ?> addFilterComponent(final FilterComponentSupplier supplier)
	{
		final FilterComponent<T, ?> filterConditionComponent = supplier.create(
			this.filterableFields,
			this.fieldDataResolver,
			this.valueReUseAdapters,
			this.filterComponentSuppliers,
			this.onValueUpdated);
		this.filterContainerComponent.addFilterComponent(filterConditionComponent);
		return filterConditionComponent;
	}
	
	@Override
	protected void onAttach(final AttachEvent attachEvent)
	{
		this.addFilterComponentButtons.update(this.filterComponentSuppliers, this::addFilterComponent);
	}
	
	@Override
	public String serialize()
	{
		if(this.filterContainerComponent.getFilterComponents().isEmpty())
		{
			return null;
		}
		
		return this.serializationPrefixSupplier.get()
			+ FilterBlockComponentSerialization.LIST_START
			+ FilterBlockComponentSerialization.serializeFilterComponents(this.filterContainerComponent.getFilterComponents())
			+ FilterBlockComponentSerialization.LIST_END;
	}
	
	@Override
	public void deserializeAndApply(final String input)
	{
		final String serializationPrefix = this.serializationPrefixSupplier.get();
		// No content to process
		if(input.length() < serializationPrefix.length() + 2
			// Invalid start
			|| !input.startsWith(serializationPrefix + FilterBlockComponentSerialization.LIST_START)
			// Invalid end
			|| !input.endsWith(String.valueOf(FilterBlockComponentSerialization.LIST_END)))
		{
			return;
		}
		
		final List<String> subElements = FilterBlockComponentSerialization.deserializeSubElements(
			// _OPERATOR(<content>)
			input.substring(serializationPrefix.length() + 1, input.length() - 1));
		
		FilterBlockComponentSerialization.deserializeFilterComponents(
			subElements,
			this.filterComponentSuppliers,
			this::addFilterComponent);
	}
	
	@Override
	public boolean test(final T item)
	{
		return this.testAggregate.test(
			this.filterContainerComponent.getFilterComponents().stream(), c -> c.test(item));
	}
}
