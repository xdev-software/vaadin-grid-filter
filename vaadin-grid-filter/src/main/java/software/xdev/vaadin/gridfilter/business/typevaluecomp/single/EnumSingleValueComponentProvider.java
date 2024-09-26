package software.xdev.vaadin.gridfilter.business.typevaluecomp.single;

import java.util.List;
import java.util.Objects;
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
	
	@Override
	public String serialize(final TypeValueComponentData<SingleValue<? extends Enum<?>>> typeValueComponentData)
	{
		return typeValueComponentData.binder().getBean().getValue().name();
	}
	
	@Override
	public void deserializeAndApply(
		final String input,
		final TypeValueComponentData<SingleValue<? extends Enum<?>>> typeValueComponentData)
	{
		if(typeValueComponentData.component() instanceof final ComboBox<?> comboBox)
		{
			comboBox.getListDataView().getItems()
				.filter(Enum.class::isInstance)
				.map(Enum.class::cast)
				.filter(e -> Objects.equals(e.name(), input))
				.findFirst()
				.ifPresent(v -> {
					final Binder<SingleValue<? extends Enum<?>>> binder = typeValueComponentData.binder();
					binder.getBean().setValueUnchecked(v);
					binder.refreshFields();
				});
		}
	}
}
