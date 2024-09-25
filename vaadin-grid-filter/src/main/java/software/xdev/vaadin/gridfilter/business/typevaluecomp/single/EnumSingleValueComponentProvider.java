package software.xdev.vaadin.gridfilter.business.typevaluecomp.single;

import java.util.List;
import java.util.Set;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;

import software.xdev.vaadin.gridfilter.business.typevaluecomp.DefaultTypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentData;
import software.xdev.vaadin.gridfilter.business.value.SingleValue;


public class EnumSingleValueComponentProvider extends DefaultTypeValueComponentProvider<SingleValue<? extends Enum<?>>>
{
	public EnumSingleValueComponentProvider()
	{
		super(Set.of(Enum.class));
	}
	
	@Override
	public SingleValue<? extends Enum<?>> createEmptyValueContainer()
	{
		return new SingleValue<>();
	}
	
	@Override
	public Class<?> valueContainerClass()
	{
		return SingleValue.class;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"}) // Type erasure is required otherwise the UI fails
	@Override
	public TypeValueComponentData<SingleValue<? extends Enum<?>>> getNewComponentData(final Class<?> clazz)
	{
		final ComboBox comboBox = new ComboBox<>();
		comboBox.setItems(List.of(clazz.getEnumConstants()));
		
		final Binder<SingleValue<? extends Enum<?>>> binder = new Binder<>();
		
		this.bindBinderTypeSafe(
			binder,
			comboBox,
			SingleValue::getValue,
			SingleValue::setValueUnchecked
		);
		
		return new TypeValueComponentData<>(binder, comboBox);
	}
	
	protected <B, F> void bindBinderTypeSafe(
		final Binder<B> binder,
		final HasValue<?, F> field,
		final ValueProvider<B, F> getter,
		final Setter<B, F> setter)
	{
		binder.forField(field)
			.asRequired()
			.bind(getter, setter);
	}
}
