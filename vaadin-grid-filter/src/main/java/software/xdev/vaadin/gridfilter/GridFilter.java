package software.xdev.vaadin.gridfilter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;

import software.xdev.vaadin.gridfilter.business.operation.ContainsOp;
import software.xdev.vaadin.gridfilter.business.operation.EqualsOp;
import software.xdev.vaadin.gridfilter.business.operation.GreaterThanOp;
import software.xdev.vaadin.gridfilter.business.operation.IsEmptyOp;
import software.xdev.vaadin.gridfilter.business.operation.LessThanOp;
import software.xdev.vaadin.gridfilter.business.operation.Operation;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.novalue.NoValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.single.EnumSingleValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.single.SingleValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.single.SingleValueNotRequiredComponentProvider;


public class GridFilter<T> extends VerticalLayout
{
	private final List<Operation<?>> availableOperations = new ArrayList<>();
	private final List<TypeValueComponentProvider<?>> availableTypeValueComponentProviders = new ArrayList<>();
	private final List<FilterableField<T, ?>> filterableFields = new ArrayList<>();
	
	private final Map<Operation<?>, List<TypeValueComponentProvider<?>>> cacheOperationTypeValueComponents =
		Collections.synchronizedMap(new LinkedHashMap<>());
	private final Map<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> cacheField =
		Collections.synchronizedMap(new LinkedHashMap<>());
	
	private final Grid<T> grid;
	private final List<FieldFilterConditionComponent<T>> fieldFilterConditionComponents = new ArrayList<>();
	
	private final VerticalLayout filterConditionsContainer = new VerticalLayout();
	
	public GridFilter(final Grid<T> grid)
	{
		this.grid = grid;
		// TODO: UI
		// TODO: AND / OR FILTERS
		
		this.filterConditionsContainer.setPadding(false);
		this.add(this.filterConditionsContainer);
		
		this.add(new Button("Add", ev -> this.addFilterConditionComponent()));
		
		this.setPadding(false);
	}
	
	protected void addFilterConditionComponent()
	{
		this.fieldFilterConditionComponents.add(this.createNewFilterConditionComponent());
		
		this.filterConditionsContainer.removeAll();
		this.fieldFilterConditionComponents.forEach(this.filterConditionsContainer::add);
	}
	
	protected void onFilterUpdate()
	{
		this.grid.getListDataView()
			.setFilter(item -> this.fieldFilterConditionComponents.stream().allMatch(c -> c.test(item)));
	}
	
	protected FieldFilterConditionComponent<T> createNewFilterConditionComponent()
	{
		return new FieldFilterConditionComponent<>(
			this.filterableFields,
			this::getForField,
			this::onFilterUpdate
		);
	}
	
	protected Map<Operation<?>, TypeValueComponentProvider<?>> getForField(final FilterableField<T, ?> field)
	{
		if(field == null)
		{
			return Map.of();
		}
		
		return this.cacheField.computeIfAbsent(field, f -> {
			// Find all operations that are applicable for the current field
			final List<Operation<?>> operations = this.availableOperations.stream()
				.filter(o -> o.canHandle(f.clazz()))
				.toList();
			
			// Query or create cache about Operations and matching TypeValueComponents
			// WITHOUT the type (otherwise it's not cacheable)
			final Map<Operation<?>, List<TypeValueComponentProvider<?>>> operationTypeValueComponents =
				operations.stream()
					.map(op -> Map.entry(op, this.cacheOperationTypeValueComponents.computeIfAbsent(op, o ->
						this.availableTypeValueComponentProviders.stream()
							.filter(t -> Objects.equals(t.valueContainerClass(), o.valueContainerClass()))
							.toList()
					)))
					.collect(Collectors.toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(a, b) -> a,
						LinkedHashMap::new));
			
			// Filter for applicable TypeValueComponents
			return operationTypeValueComponents.entrySet()
				.stream()
				.map(e -> Map.entry(
					e.getKey(),
					e.getValue().stream()
						.filter(t -> t.canHandle(f.clazz()))
						.findFirst()))
				.filter(e -> e.getValue().isPresent())
				.collect(Collectors.toMap(
					Map.Entry::getKey,
					e -> e.getValue().orElseThrow(),
					(a, b) -> a,
					LinkedHashMap::new));
		});
	}
	
	protected GridFilter<T> invalidateCache()
	{
		this.cacheOperationTypeValueComponents.clear();
		this.cacheField.clear();
		return this;
	}
	
	// region Config
	
	public GridFilter<T> addOperation(final Operation<?> operation)
	{
		this.availableOperations.add(operation);
		return this.invalidateCache();
	}
	
	public GridFilter<T> addOperations(final Collection<Operation<?>> operations)
	{
		this.availableOperations.addAll(operations);
		return this.invalidateCache();
	}
	
	public GridFilter<T> clearOperations()
	{
		this.availableOperations.clear();
		return this.invalidateCache();
	}
	
	public GridFilter<T> addTypeValueComponent(final TypeValueComponentProvider<?> typeValueComponentProvider)
	{
		this.availableTypeValueComponentProviders.add(typeValueComponentProvider);
		return this.invalidateCache();
	}
	
	public GridFilter<T> addTypeValueComponents(final Collection<TypeValueComponentProvider<?>> typeValueComponentProviders)
	{
		this.availableTypeValueComponentProviders.addAll(typeValueComponentProviders);
		return this.invalidateCache();
	}
	
	public GridFilter<T> clearTypeValueComponents()
	{
		this.availableTypeValueComponentProviders.clear();
		return this.invalidateCache();
	}
	
	public <S> GridFilter<T> withSearchableField(
		final String name,
		final Function<T, S> keyExtractor,
		final Class<S> clazz)
	{
		this.filterableFields.add(new FilterableField<>(name, keyExtractor, clazz));
		return this;
	}
	
	// endregion
	
	public static <T> GridFilter<T> createDefault(final Grid<T> grid)
	{
		return new GridFilter<>(grid)
			.addOperations(List.of(
				new EqualsOp(),
				new GreaterThanOp(),
				new LessThanOp(),
				new ContainsOp(),
				new IsEmptyOp()
			))
			.addTypeValueComponents(List.of(
				new NoValueComponentProvider(),
				new EnumSingleValueComponentProvider(),
				new SingleValueNotRequiredComponentProvider<>(Boolean.class, Checkbox::new),
				new SingleValueComponentProvider<>(String.class, TextField::new),
				new SingleValueComponentProvider<>(Double.class, NumberField::new),
				new SingleValueComponentProvider<>(Integer.class, IntegerField::new),
				new SingleValueComponentProvider<>(BigDecimal.class, BigDecimalField::new),
				new SingleValueComponentProvider<>(LocalDate.class, DatePicker::new),
				new SingleValueComponentProvider<>(LocalDateTime.class, DateTimePicker::new),
				new SingleValueComponentProvider<>(LocalTime.class, TimePicker::new)
			));
	}
}
