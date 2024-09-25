package software.xdev.vaadin.gridfilter.business.typevaluecomp;

import java.util.function.Consumer;

import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import software.xdev.vaadin.gridfilter.business.value.ValueContainer;


@Deprecated // TODO REMOVE
public abstract class DefaultTypeValueComponent<V extends ValueContainer>
{
	protected final Binder<V> binder = new Binder<>();
	protected Registration binderValueChangeRegistration;
	
	public void load(final V value)
	{
		this.binder.setBean(value);
	}
	
	public V saveIfValid()
	{
		if(!this.binder.isValid())
		{
			return null;
		}
		return this.binder.getBean();
	}
	
	public void setValidationStateChangedListener(final Consumer<Boolean> onValidationStateChanged)
	{
		if(this.binderValueChangeRegistration != null)
		{
			this.binderValueChangeRegistration.remove();
			this.binderValueChangeRegistration = null;
		}
		if(onValidationStateChanged != null)
		{
			this.binder.addValueChangeListener(ev ->
				onValidationStateChanged.accept(((ValueContainer)ev.getValue()).isValid()));
		}
	}
}
