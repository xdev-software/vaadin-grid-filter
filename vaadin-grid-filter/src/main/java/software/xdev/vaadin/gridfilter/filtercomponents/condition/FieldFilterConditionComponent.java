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
package software.xdev.vaadin.gridfilter.filtercomponents.condition;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;

import software.xdev.vaadin.gridfilter.FilterableField;
import software.xdev.vaadin.gridfilter.business.operation.Operation;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentData;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.value.ValueContainer;
import software.xdev.vaadin.gridfilter.business.value.reuse.ValueReUseAdapter;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponent;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponentStyles;


@SuppressWarnings("java:S1948")
public class FieldFilterConditionComponent<T> extends FilterComponent<T, HorizontalLayout>
{
	protected static final String SER_SEPARATOR = " ";
	
	protected final ComboBox<FilterableField<T, ?>> cbField = new ComboBox<>();
	protected final ComboBox<Operation<?>> cbOperation = new ComboBox<>();
	protected final HorizontalLayout operationDetailsContainer = new HorizontalLayout();
	
	protected Map<Operation<?>, TypeValueComponentProvider<?>> currentOperationsData;
	protected final AtomicReference<Binder<? extends ValueContainer>> refCurrentBinder = new AtomicReference<>();
	protected final AtomicReference<TypeValueComponentProvider<?>> refTypeValueComponentProvider =
		new AtomicReference<>();
	protected final AtomicReference<TypeValueComponentData<?>> refTypeValueComponentData = new AtomicReference<>();
	
	protected final Function<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> fieldDataResolver;
	protected final Map<Class<? extends ValueContainer>, Set<ValueReUseAdapter<?>>> valueReUseAdapters;
	protected final Runnable onValueUpdated;
	
	public FieldFilterConditionComponent(
		final List<FilterableField<T, ?>> filterableFields,
		final Function<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> fieldDataResolver,
		final Map<Class<? extends ValueContainer>, Set<ValueReUseAdapter<?>>> valueReUseAdapters,
		final Runnable onValueUpdated)
	{
		this.fieldDataResolver = fieldDataResolver;
		this.valueReUseAdapters = valueReUseAdapters;
		this.onValueUpdated = onValueUpdated;
		
		this.cbField.setItemLabelGenerator(FilterableField::name);
		this.cbField.setItems(filterableFields);
		this.cbField.addValueChangeListener(ev -> this.onFieldChanged(ev.getValue(), ev.isFromClient()));
		this.cbField.addClassNames(FilterFieldConditionComponentStyles.CB_FIELD);
		
		this.cbOperation.setItemLabelGenerator(Operation::identifier);
		this.cbOperation.addValueChangeListener(ev -> this.onOperationChanged(ev.getValue(), ev.isFromClient()));
		this.cbOperation.addClassNames(FilterFieldConditionComponentStyles.CB_OPERATION);
		
		this.operationDetailsContainer.addClassNames(FilterFieldConditionComponentStyles.DETAILS);
		
		this.getContent().add(this.cbField, this.cbOperation, this.operationDetailsContainer);
		this.getContent().setAlignItems(FlexComponent.Alignment.BASELINE);
		this.getContent().addClassNames(
			FilterComponentStyles.FILTER_COMPONENT,
			FilterFieldConditionComponentStyles.FILTER_FIELD_CONDITION_COMPONENT);
	}
	
	protected void onFieldChanged(final FilterableField<T, ?> value, final boolean isFromClient)
	{
		this.currentOperationsData =
			Optional.ofNullable(value)
				.map(this.fieldDataResolver)
				.orElseGet(Map::of);
		
		this.cbOperation.setItems(this.currentOperationsData.keySet());
		this.runValueChanged(isFromClient);
	}
	
	protected void onOperationChanged(final Operation<?> value, final boolean isFromClient)
	{
		final Binder<? extends ValueContainer> prevBinder = this.refCurrentBinder.getAndSet(null);
		final Optional<? extends ValueContainer> optPrevValueContainer = Optional.ofNullable(prevBinder)
			.map(Binder::getBean);
		this.operationDetailsContainer.removeAll();
		this.refTypeValueComponentProvider.set(null);
		this.refTypeValueComponentData.set(null);
		
		Optional.ofNullable(value)
			.map(this.currentOperationsData::get)
			.map(p -> {
				this.refTypeValueComponentProvider.set(p);
				return p;
			})
			.map(p -> p.getNewComponentDataWithDefaults(this.cbField.getValue().clazz()))
			.ifPresent(componentData -> {
				this.refTypeValueComponentData.set(componentData);
				final Binder<? extends ValueContainer> binder = componentData.binder();
				
				// Check if for previous value
				optPrevValueContainer.ifPresent(prevValue ->
					// Check if for matching reuse value adapters
					Optional.ofNullable(this.valueReUseAdapters.get(binder.getBean().getClass()))
						.ifPresent(adapters -> {
							// Check if any adapter can be applied
							if(adapters.stream().anyMatch(a -> a.tryReUseUnchecked(prevValue, binder.getBean())))
							{
								// If reused -> Refresh
								binder.refreshFields();
							}
						}));
				
				binder.addValueChangeListener(ev2 -> this.runValueChanged(ev2.isFromClient()));
				
				this.refCurrentBinder.set(binder);
				
				Optional.ofNullable(componentData.component())
					.ifPresent(this.operationDetailsContainer::add);
			});
		
		this.runValueChanged(isFromClient);
	}
	
	@Override
	public String serialize()
	{
		final FilterableField<T, ?> field = this.cbField.getValue();
		final Operation<?> operation = this.cbOperation.getValue();
		final TypeValueComponentProvider<?> typeValueComponentProvider = this.refTypeValueComponentProvider.get();
		final TypeValueComponentData<?> typeValueComponentData = this.refTypeValueComponentData.get();
		
		if(field == null || operation == null || typeValueComponentProvider == null || typeValueComponentData == null)
		{
			return null;
		}
		
		try
		{
			return Stream.of(
					field.identifier(),
					URLEncoder.encode(operation.identifier(), StandardCharsets.UTF_8),
					Optional.ofNullable(typeValueComponentProvider.serializeUnchecked(typeValueComponentData))
						.map(s -> URLEncoder.encode(s, StandardCharsets.UTF_8))
						.orElse(null)
				)
				.filter(Objects::nonNull)
				.collect(Collectors.joining(SER_SEPARATOR));
		}
		catch(final Exception ex)
		{
			// Something was not serializable
			return null;
		}
	}
	
	@Override
	public void deserializeAndApply(final String input)
	{
		final String[] parts = input.split(SER_SEPARATOR);
		if(parts.length < 2 || parts.length > 3)
		{
			// Invalid amount of parts -> Abort
			return;
		}
		
		try
		{
			// 0 -> Set field
			this.cbField.getListDataView().getItems()
				.filter(f -> Objects.equals(f.identifier(), parts[0]))
				.findFirst()
				.ifPresent(field -> {
					this.cbField.setValue(field);
					
					// 1 -> Set operation
					final String decOperation = URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
					this.cbOperation.getListDataView().getItems()
						.filter(o -> Objects.equals(o.identifier(), decOperation))
						.findFirst()
						.ifPresent(operation -> {
							this.cbOperation.setValue(operation);
							
							// 2 -> (if present) Set value
							if(parts.length == 3)
							{
								final String decValue = URLDecoder.decode(parts[2], StandardCharsets.UTF_8);
								Optional.ofNullable(this.refTypeValueComponentProvider.get())
									.ifPresent(p -> Optional.ofNullable(this.refTypeValueComponentData.get())
										.ifPresent(data -> {
											p.deserializeAndApplyUnchecked(decValue, data);
											data.binder().refreshFields();
										}));
							}
						});
				});
		}
		catch(final Exception ex)
		{
			// Failed to restore state. This might be due to invalid input. Ignore it
		}
	}
	
	@Override
	public boolean test(final T item)
	{
		if(this.cbField.isEmpty() || this.cbOperation.isEmpty() || this.refCurrentBinder.get() == null)
		{
			// Bypass
			return true;
		}
		
		final ValueContainer data = this.refCurrentBinder.get().getBean();
		if(!data.isValid())
		{
			// Bypass
			return true;
		}
		
		return this.cbOperation.getValue().testUnchecked(
			this.cbField.getValue().keyExtractor().apply(item),
			data);
	}
	
	protected void runValueChanged(final boolean isFromClient)
	{
		if(isFromClient)
		{
			this.onValueUpdated.run();
		}
	}
}
