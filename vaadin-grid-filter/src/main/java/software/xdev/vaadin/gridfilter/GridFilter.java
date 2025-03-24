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
package software.xdev.vaadin.gridfilter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.shared.Registration;

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
import software.xdev.vaadin.gridfilter.business.value.ValueContainer;
import software.xdev.vaadin.gridfilter.business.value.reuse.SingleValueReUseAdapter;
import software.xdev.vaadin.gridfilter.business.value.reuse.ValueReUseAdapter;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterBlockComponentSerialization;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponent;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponentSerialization;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponentSupplier;
import software.xdev.vaadin.gridfilter.filtercomponents.block.FilterANDComponentSupplier;
import software.xdev.vaadin.gridfilter.filtercomponents.block.FilterNOTComponentSupplier;
import software.xdev.vaadin.gridfilter.filtercomponents.block.FilterORComponentSupplier;
import software.xdev.vaadin.gridfilter.filtercomponents.condition.FieldFilterConditionComponentSupplier;


@SuppressWarnings("java:S1948")
@CssImport(GridFilterStyles.LOCATION)
public class GridFilter<T>
	extends VerticalLayout
	implements FilterComponentSerialization
{
	protected static final int START_DEPTH = 1;
	
	protected final List<Operation<?>> availableOperations = new ArrayList<>();
	protected final List<TypeValueComponentProvider<?>> availableTypeValueComponentProviders = new ArrayList<>();
	protected final Map<Class<? extends ValueContainer>, Set<ValueReUseAdapter<?>>> valueReUseAdapters =
		new HashMap<>();
	protected final List<FilterComponentSupplier> filterComponentSuppliers = new ArrayList<>();
	protected final List<FilterableField<T, ?>> filterableFields = new ArrayList<>();
	protected int maxNestedDepth = 10;
	protected GridFilterLocalizationConfig localizationConfig = new GridFilterLocalizationConfig();
	
	protected final Map<Operation<?>, List<TypeValueComponentProvider<?>>> cacheOperationTypeValueComponents =
		Collections.synchronizedMap(new LinkedHashMap<>());
	protected final Map<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> cacheField =
		Collections.synchronizedMap(new LinkedHashMap<>());
	
	protected final Grid<T> grid;
	
	protected final FilterContainerComponent<T> filterContainerComponent =
		new FilterContainerComponent<>(this::onFilterUpdate, false);
	protected final AddFilterComponentsButtons addFilterComponentButtons = new AddFilterComponentsButtons();
	
	/**
	 * A function that defines how the filter is applied to the {@link Grid}.
	 * <p>
	 * Please note that the default will only work for {@link com.vaadin.flow.data.provider.ListDataProvider} or
	 * derivates
	 * </p>
	 */
	protected BiConsumer<Grid<T>, List<FilterComponent<T, ?>>> applyFilter = (gr, components)
		-> gr.getListDataView().setFilter(item -> components.stream().allMatch(c -> c.test(item)));
	
	public GridFilter(final Grid<T> grid)
	{
		this.grid = grid;
		
		this.add(this.filterContainerComponent, this.addFilterComponentButtons);
		
		this.setPadding(false);
		this.setSpacing(false);
		
		this.addClassNames(GridFilterStyles.GRID_FILTER);
	}
	
	/**
	 * @see #applyFilter
	 */
	public GridFilter<T> withApplyPredicateFilter(final BiConsumer<Grid<T>, Predicate<T>> applyPredicateFilter)
	{
		Objects.requireNonNull(applyPredicateFilter);
		this.applyFilter = (gr, components)
			-> applyPredicateFilter.accept(gr, item -> components.stream().allMatch(c -> c.test(item)));
		return this;
	}
	
	/**
	 * @see #applyFilter
	 */
	public GridFilter<T> withApplyFilter(final BiConsumer<Grid<T>, List<FilterComponent<T, ?>>> applyFilter)
	{
		this.applyFilter = Objects.requireNonNull(applyFilter);
		return this;
	}
	
	@SuppressWarnings("java:S1452")
	public FilterComponent<T, ?> addFilterComponent(final FilterComponentSupplier supplier)
	{
		final FilterComponent<T, ?> filterComponent = supplier.create(
			this.localizationConfig,
			this.filterableFields,
			this::getForField,
			this.valueReUseAdapters,
			this.filterComponentSuppliers,
			this::onFilterUpdate,
			START_DEPTH,
			this.maxNestedDepth);
		this.filterContainerComponent.addFilterComponent(filterComponent);
		return filterComponent;
	}
	
	protected void onFilterUpdate()
	{
		this.applyFilterToGrid();
		
		this.fireEvent(new FilterChangedEvent<>(this, false));
	}
	
	protected void applyFilterToGrid()
	{
		this.applyFilter.accept(this.grid, this.filterContainerComponent.getFilterComponents());
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Registration addFilterChangedListener(final ComponentEventListener<FilterChangedEvent<T>> listener)
	{
		return this.addListener(FilterChangedEvent.class, (ComponentEventListener)listener);
	}
	
	public static class FilterChangedEvent<T> extends ComponentEvent<GridFilter<T>>
	{
		public FilterChangedEvent(final GridFilter<T> source, final boolean fromClient)
		{
			super(source, fromClient);
		}
	}
	
	@Override
	protected void onAttach(final AttachEvent attachEvent)
	{
		this.addFilterComponentButtons.update(
			this.localizationConfig,
			this.filterComponentSuppliers,
			this::addFilterComponent,
			START_DEPTH,
			this.maxNestedDepth);
	}
	
	@SuppressWarnings("java:S1452") // No
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
	
	@Override
	public String serialize()
	{
		return FilterBlockComponentSerialization.serializeFilterComponents(
			this.filterContainerComponent.getFilterComponents());
	}
	
	@Override
	public void deserializeAndApply(final String input)
	{
		FilterBlockComponentSerialization.deserializeFilterComponents(
			FilterBlockComponentSerialization.deserializeSubElements(input),
			this.filterComponentSuppliers,
			this::addFilterComponent);
		this.onFilterUpdate();
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
	
	public GridFilter<T> addTypeValueComponents(
		final Collection<TypeValueComponentProvider<?>> typeValueComponentProviders)
	{
		this.availableTypeValueComponentProviders.addAll(typeValueComponentProviders);
		return this.invalidateCache();
	}
	
	public GridFilter<T> clearTypeValueComponents()
	{
		this.availableTypeValueComponentProviders.clear();
		return this.invalidateCache();
	}
	
	public GridFilter<T> addValueReUseAdapter(final ValueReUseAdapter<?> valueReUseAdapter)
	{
		return this.addValueReUseAdapters(List.of(valueReUseAdapter));
	}
	
	public GridFilter<T> addValueReUseAdapters(final Collection<ValueReUseAdapter<?>> valueReUseAdapters)
	{
		valueReUseAdapters.forEach(adapter -> {
			final Set<ValueReUseAdapter<?>> adapters = this.valueReUseAdapters.computeIfAbsent(
				adapter.newValueClass(),
				ignored -> new HashSet<>());
			adapters.add(adapter);
		});
		return this;
	}
	
	public GridFilter<T> clearValueReUseAdapter()
	{
		this.valueReUseAdapters.clear();
		return this.invalidateCache();
	}
	
	public GridFilter<T> addFilterComponentSupplier(final FilterComponentSupplier filterComponentSupplier)
	{
		this.filterComponentSuppliers.add(filterComponentSupplier);
		return this;
	}
	
	public GridFilter<T> addFilterComponentSuppliers(
		final Collection<FilterComponentSupplier> filterComponentSuppliers)
	{
		this.filterComponentSuppliers.addAll(filterComponentSuppliers);
		return this;
	}
	
	public GridFilter<T> clearFilterComponentSuppliers()
	{
		this.filterComponentSuppliers.clear();
		return this;
	}
	
	/**
	 * @apiNote This method should not be used if name is dynamic (e.g. translated).<br/>In this case use
	 * {@link #withFilterableField(String, String, Function, Class)}
	 */
	public <S> GridFilter<T> withFilterableField(
		final String name,
		final Function<T, S> keyExtractor,
		final Class<S> clazz)
	{
		return this.withFilterableField(
			name,
			name.chars()
				.filter(c -> Character.isLetter(c) || Character.isDigit(c))
				.collect(
					() -> new StringBuilder(name.length()),
					StringBuilder::appendCodePoint,
					StringBuilder::append)
				.toString(),
			keyExtractor,
			clazz);
	}
	
	public <S> GridFilter<T> withFilterableField(
		final String name,
		final String identifier,
		final Function<T, S> keyExtractor,
		final Class<S> clazz)
	{
		this.filterableFields.add(new FilterableField<>(name, identifier, keyExtractor, clazz));
		return this;
	}
	
	public GridFilter<T> withMaxNestedDepth(final int maxNestedDepth)
	{
		if(maxNestedDepth < START_DEPTH)
		{
			throw new IllegalArgumentException("Invalid depth");
		}
		this.maxNestedDepth = maxNestedDepth;
		return this;
	}
	
	public GridFilter<T> withLocalizationConfig(final GridFilterLocalizationConfig localizationConfig)
	{
		this.localizationConfig = localizationConfig;
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
				new SingleValueNotRequiredComponentProvider<>(
					Boolean.class,
					Checkbox::new,
					b -> Boolean.TRUE.equals(b) ? "1" : "0",
					"1"::equals),
				new SingleValueComponentProvider<>(
					String.class,
					TextField::new,
					s -> s),
				new SingleValueComponentProvider<>(
					Double.class,
					NumberField::new,
					Double::parseDouble),
				new SingleValueComponentProvider<>(
					Integer.class,
					IntegerField::new,
					Integer::parseInt),
				new SingleValueComponentProvider<>(
					BigDecimal.class,
					BigDecimalField::new,
					BigDecimal::new),
				new SingleValueComponentProvider<>(
					LocalDate.class,
					DatePicker::new,
					LocalDate::parse),
				new SingleValueComponentProvider<>(
					LocalDateTime.class,
					DateTimePicker::new,
					LocalDateTime::parse),
				new SingleValueComponentProvider<>(
					LocalTime.class,
					TimePicker::new,
					LocalTime::parse)
			))
			.addValueReUseAdapter(new SingleValueReUseAdapter())
			.addFilterComponentSuppliers(List.of(
				new FieldFilterConditionComponentSupplier(),
				new FilterORComponentSupplier(),
				new FilterANDComponentSupplier(),
				new FilterNOTComponentSupplier()));
	}
}
