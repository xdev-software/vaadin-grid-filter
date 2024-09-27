package software.xdev.vaadin.gridfilter.filtercomponents.block;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import software.xdev.vaadin.gridfilter.FilterableField;
import software.xdev.vaadin.gridfilter.business.operation.Operation;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.value.ValueContainer;
import software.xdev.vaadin.gridfilter.business.value.reuse.ValueReUseAdapter;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponent;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponentSupplier;


public abstract class FilterBlockComponentSupplier implements FilterComponentSupplier
{
	@Override
	public String display()
	{
		return "AND";
	}
	
	@Override
	public boolean canCreateNested()
	{
		return true;
	}
	
	protected abstract <T> boolean testAggregate(
		final Stream<FilterComponent<T, ?>> stream,
		final Predicate<FilterComponent<T, ?>> predicate);
	
	@Override
	public <T> FilterComponent<T, ?> create(
		final List<FilterableField<T, ?>> filterableFields,
		final Function<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> fieldDataResolver,
		final Map<Class<? extends ValueContainer>, Set<ValueReUseAdapter<?>>> valueReUseAdapters,
		final List<FilterComponentSupplier> filterComponentSuppliers,
		final Runnable onValueUpdated,
		final int nestedDepth,
		final int maxNestedDepth)
	{
		return new FilterBlockComponent<>(
			filterableFields,
			fieldDataResolver,
			valueReUseAdapters,
			filterComponentSuppliers,
			onValueUpdated,
			this::testAggregate,
			this.display(),
			this::serializationPrefix,
			nestedDepth,
			maxNestedDepth);
	}
}
