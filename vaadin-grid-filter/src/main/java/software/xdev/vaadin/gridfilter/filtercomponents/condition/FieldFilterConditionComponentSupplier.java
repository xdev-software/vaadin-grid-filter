package software.xdev.vaadin.gridfilter.filtercomponents.condition;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import software.xdev.vaadin.gridfilter.FilterableField;
import software.xdev.vaadin.gridfilter.business.operation.Operation;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.value.ValueContainer;
import software.xdev.vaadin.gridfilter.business.value.reuse.ValueReUseAdapter;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponent;
import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponentSupplier;


public class FieldFilterConditionComponentSupplier implements FilterComponentSupplier
{
	@Override
	public String display()
	{
		return "condition";
	}
	
	@Override
	public String serializationPrefix()
	{
		return "";
	}
	
	@Override
	public <T> FilterComponent<T, ?> create(
		final List<FilterableField<T, ?>> filterableFields,
		final Function<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> fieldDataResolver,
		final Map<Class<? extends ValueContainer>, Set<ValueReUseAdapter<?>>> valueReUseAdapters,
		final List<FilterComponentSupplier> filterComponentSuppliers,
		final Runnable onValueUpdated)
	{
		return new FieldFilterConditionComponent<>(
			filterableFields,
			fieldDataResolver,
			valueReUseAdapters,
			onValueUpdated);
	}
}
