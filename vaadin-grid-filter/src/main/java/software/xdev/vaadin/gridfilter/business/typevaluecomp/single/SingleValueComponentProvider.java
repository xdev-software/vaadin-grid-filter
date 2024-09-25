package software.xdev.vaadin.gridfilter.business.typevaluecomp.single;

import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.binder.Binder;

import software.xdev.vaadin.gridfilter.business.typevaluecomp.DefaultTypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentData;
import software.xdev.vaadin.gridfilter.business.value.SingleValue;


public class SingleValueComponentProvider<T, C extends Component & HasValue<?, T>>
	extends DefaultTypeValueComponentProvider<SingleValue<T>>
{
	protected final Supplier<C> componentSupplier;
	
	public SingleValueComponentProvider(final Class<T> clazz, final Supplier<C> componentSupplier)
	{
		super(Set.of(clazz));
		this.componentSupplier = Objects.requireNonNull(componentSupplier);
	}
	
	@Override
	public SingleValue<T> createEmptyValueContainer()
	{
		return new SingleValue<>();
	}
	
	@Override
	public Class<?> valueContainerClass()
	{
		return SingleValue.class;
	}
	
	@Override
	public TypeValueComponentData<SingleValue<T>> getNewComponentData(final Class<?> clazz)
	{
		final Binder<SingleValue<T>> binder = new Binder<>();
		final C component = this.componentSupplier.get();
		
		final Binder.BindingBuilder<SingleValue<T>, T> bindingBuilder = binder.forField(component);
		this.handleBindingBuilder(bindingBuilder);
		bindingBuilder.bind(SingleValue::getValue, SingleValue::setValue);
		
		return new TypeValueComponentData<>(binder, component);
	}
	
	protected void handleBindingBuilder(final Binder.BindingBuilder<SingleValue<T>, T> bindingBuilder)
	{
		bindingBuilder.asRequired();
	}
}
