package software.xdev.vaadin.gridfilter.business.typevaluecomp.single;

import java.util.function.Supplier;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.binder.Binder;

import software.xdev.vaadin.gridfilter.business.value.SingleValue;


public class SingleValueNotRequiredComponentProvider<T, C extends Component & HasValue<?, T>>
	extends SingleValueComponentProvider<T, C>
{
	public SingleValueNotRequiredComponentProvider(final Class<T> clazz, final Supplier<C> componentSupplier)
	{
		super(clazz, componentSupplier);
	}
	
	@Override
	protected void handleBindingBuilder(final Binder.BindingBuilder<SingleValue<T>, T> bindingBuilder)
	{
		// No require is added
	}
}
