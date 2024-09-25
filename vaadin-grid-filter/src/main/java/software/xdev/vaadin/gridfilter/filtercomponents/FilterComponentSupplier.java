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


public interface FilterComponentSupplier
{
	String display(); // TODO I18N
	
	<T> FilterComponent<T> create(
		List<FilterableField<T, ?>> filterableFields,
		Function<FilterableField<T, ?>, Map<Operation<?>, TypeValueComponentProvider<?>>> fieldDataResolver,
		Map<Class<? extends ValueContainer>, Set<ValueReUseAdapter<?>>> valueReUseAdapters,
		List<FilterComponentSupplier> filterComponentSuppliers,
		Runnable onValueUpdated);
}
