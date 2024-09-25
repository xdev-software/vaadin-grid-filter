package software.xdev.vaadin.gridfilter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;

import software.xdev.vaadin.gridfilter.business.operation.Operation;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.value.ValueContainer;
import software.xdev.vaadin.gridfilter.business.value.reuse.ValueReUseAdapter;


public class FieldFilterConditionComponent<T> extends HorizontalLayout
{
	protected final ComboBox<FilterableField<T, ?>> cbField = new ComboBox<>();
	protected final ComboBox<Operation<?>> cbOperation = new ComboBox<>();
	protected final HorizontalLayout operationDetailsContainer = new HorizontalLayout();
	
	protected Map<Operation<?>, TypeValueComponentProvider<?>> currentOperationsData;
	protected final AtomicReference<Binder<? extends ValueContainer>> refCurrentBinder = new AtomicReference<>();
	
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
		this.cbField.addValueChangeListener(ev -> this.onFieldChanged(ev.getValue()));
		
		this.add(this.cbField);
		
		this.cbOperation.setItemLabelGenerator(Operation::display);
		this.cbOperation.addValueChangeListener(ev -> this.onOperationChanged(ev.getValue()));
		
		this.add(this.cbOperation);
		
		this.add(this.operationDetailsContainer);
		
		this.setAlignItems(Alignment.BASELINE);
	}
	
	protected void onFieldChanged(final FilterableField<T, ?> value)
	{
		this.currentOperationsData =
			Optional.ofNullable(value)
				.map(this.fieldDataResolver)
				.orElseGet(Map::of);
		
		this.cbOperation.setItems(this.currentOperationsData.keySet());
		this.onValueUpdated.run();
	}
	
	protected void onOperationChanged(final Operation<?> value)
	{
		final Binder<? extends ValueContainer> prevBinder = this.refCurrentBinder.getAndSet(null);
		final Optional<? extends ValueContainer> optPrevValueContainer = Optional.ofNullable(prevBinder)
			.map(Binder::getBean);
		this.operationDetailsContainer.removeAll();
		
		Optional.ofNullable(value)
			.map(this.currentOperationsData::get)
			.map(p -> p.getNewComponentDataWithDefaults(this.cbField.getValue().clazz()))
			.ifPresent(componentData -> {
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
				
				binder.addValueChangeListener(ev2 -> this.onValueUpdated.run());
				
				this.refCurrentBinder.set(binder);
				
				Optional.ofNullable(componentData.component()).ifPresent(this.operationDetailsContainer::add);
			});
		
		this.onValueUpdated.run();
	}
	
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
}
