package software.xdev.vaadin.gridfilter.business.typevaluecomp.single;

import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
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
	protected final Function<T, String> serializeFunc;
	protected final Function<String, T> deserializeFunc;
	
	public SingleValueComponentProvider(
		final Class<T> clazz,
		final Supplier<C> componentSupplier,
		final Function<String, T> deserializeFunc)
	{
		this(clazz, componentSupplier, Object::toString, deserializeFunc);
	}
	
	public SingleValueComponentProvider(
		final Class<T> clazz,
		final Supplier<C> componentSupplier,
		final Function<T, String> serializeFunc,
		final Function<String, T> deserializeFunc)
	{
		super(Set.of(clazz));
		this.componentSupplier = Objects.requireNonNull(componentSupplier);
		this.serializeFunc = Objects.requireNonNull(serializeFunc);
		this.deserializeFunc = Objects.requireNonNull(deserializeFunc);
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
	
	@Override
	public String serialize(final TypeValueComponentData<SingleValue<T>> typeValueComponentData)
	{
		final SingleValue<T> bean = typeValueComponentData.binder().getBean();
		if(bean == null || !bean.isValid())
		{
			return null;
		}
		return this.serializeFunc.apply(bean.getValue());
	}
	
	@Override
	public void deserializeAndApply(
		final String input,
		final TypeValueComponentData<SingleValue<T>> typeValueComponentData)
	{
		typeValueComponentData.binder().getBean().setValue(this.deserializeFunc.apply(input));
	}
}
