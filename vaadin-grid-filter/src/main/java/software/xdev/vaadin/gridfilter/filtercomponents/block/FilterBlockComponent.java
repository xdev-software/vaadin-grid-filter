package software.xdev.vaadin.gridfilter.filtercomponents.block;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
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
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponent;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponentSupplier;


public class FilterBlockComponent<T> extends FilterComponent<T, HorizontalLayout>
{
	protected final List<FilterableField<T, ?>> filterableFields;
	protected final Function<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> fieldDataResolver;
	protected final Map<Class<? extends ValueContainer>, Set<ValueReUseAdapter<?>>> valueReUseAdapters;
	protected final List<FilterComponentSupplier> filterComponentSuppliers;
	protected final Runnable onValueUpdated;
	
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
		final String identifierName)
	{
		this.filterableFields = filterableFields;
		this.fieldDataResolver = fieldDataResolver;
		this.valueReUseAdapters = valueReUseAdapters;
		this.filterComponentSuppliers = filterComponentSuppliers;
		this.onValueUpdated = onValueUpdated;
		this.testAggregate = testAggregate;
		
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
	
	protected void addFilterComponent(final FilterComponentSupplier supplier)
	{
		this.filterContainerComponent.addFilterComponent(supplier.create(
			this.filterableFields,
			this.fieldDataResolver,
			this.valueReUseAdapters,
			this.filterComponentSuppliers,
			this.onValueUpdated));
	}
	
	@Override
	protected void onAttach(final AttachEvent attachEvent)
	{
		this.addFilterComponentButtons.update(this.filterComponentSuppliers, this::addFilterComponent);
	}
	
	@Override
	public boolean test(final T item)
	{
		return this.testAggregate.test(
			this.filterContainerComponent.getFilterComponents().stream(), c -> c.test(item));
	}
}
