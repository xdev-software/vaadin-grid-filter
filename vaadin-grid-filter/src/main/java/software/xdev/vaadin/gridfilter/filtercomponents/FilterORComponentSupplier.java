package software.xdev.vaadin.gridfilter.filtercomponents;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import software.xdev.vaadin.gridfilter.FilterableField;
import software.xdev.vaadin.gridfilter.business.operation.Operation;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.value.ValueContainer;
import software.xdev.vaadin.gridfilter.business.value.reuse.ValueReUseAdapter;


public class FilterORComponentSupplier implements FilterComponentSupplier
{
	@Override
	public String display()
	{
		return "OR block";
	}
	
	@Override
	public <T> FilterComponent<T> create(
		final List<FilterableField<T, ?>> filterableFields,
		final Function<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> fieldDataResolver,
		final Map<Class<? extends ValueContainer>, Set<ValueReUseAdapter<?>>> valueReUseAdapters,
		final List<FilterComponentSupplier> filterComponentSuppliers,
		final Runnable onValueUpdated)
	{
		throw new UnsupportedOperationException();
	}
}
