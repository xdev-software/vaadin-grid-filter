package software.xdev.vaadin.gridfilter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;

import software.xdev.vaadin.gridfilter.business.operation.Operation;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.value.ValueContainer;


public class FieldFilterConditionComponent<T> extends HorizontalLayout
{
	private final ComboBox<FilterableField<T, ?>> cbField = new ComboBox<>();
	private final ComboBox<Operation<?>> cbOperation = new ComboBox<>();
	private final Div operationDetailsContainer = new Div();
	
	private Map<Operation<?>, TypeValueComponentProvider<?>> currentOperationsData;
	private final AtomicReference<Binder<? extends ValueContainer>> refCurrentBinder = new AtomicReference<>();
	
	public FieldFilterConditionComponent(
		final List<FilterableField<T, ?>> filterableFields,
		final Function<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> fieldDataResolver,
		final Runnable onValueUpdated)
	{
		this.cbField.setItemLabelGenerator(FilterableField::name);
		this.cbField.setItems(filterableFields);
		this.cbField.addValueChangeListener(ev -> {
			this.currentOperationsData =
				Optional.ofNullable(ev.getValue())
					.map(fieldDataResolver)
					.orElseGet(Map::of);
			
			this.cbOperation.setItems(this.currentOperationsData.keySet());
			onValueUpdated.run();
		});
		
		this.add(this.cbField);
		
		this.cbOperation.setItemLabelGenerator(Operation::display);
		this.cbOperation.addValueChangeListener(ev -> {
			this.refCurrentBinder.set(null);
			this.operationDetailsContainer.removeAll();
			
			Optional.ofNullable(ev.getValue())
				.map(this.currentOperationsData::get)
				.map(p -> p.getNewComponentDataWithDefaults(this.cbField.getValue().clazz()))
				.ifPresent(componentData -> {
					final Binder<? extends ValueContainer> binder = componentData.binder();
					
					binder.addValueChangeListener(ev2 -> onValueUpdated.run());
					
					this.refCurrentBinder.set(binder);
					
					Optional.ofNullable(componentData.component()).ifPresent(this.operationDetailsContainer::add);
				});
			
			onValueUpdated.run();
		});
		
		this.add(this.cbOperation);
		
		this.add(this.operationDetailsContainer);
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
		
		return this.cbOperation.getValue().testUnchcked(
			this.cbField.getValue().keyExtractor().apply(item),
			data);
	}
}
