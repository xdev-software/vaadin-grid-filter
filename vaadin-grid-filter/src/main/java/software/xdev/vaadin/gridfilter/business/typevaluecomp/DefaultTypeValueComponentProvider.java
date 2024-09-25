package software.xdev.vaadin.gridfilter.business.typevaluecomp;

import java.util.Collections;
import java.util.Set;

import software.xdev.vaadin.gridfilter.business.value.ValueContainer;


public abstract class DefaultTypeValueComponentProvider<V extends ValueContainer>
	implements TypeValueComponentProvider<V>
{
	protected final Set<Class<?>> supportedTypes;
	
	protected DefaultTypeValueComponentProvider(final Set<Class<?>> supportedTypes)
	{
		this.supportedTypes = Collections.unmodifiableSet(supportedTypes);
	}
	
	@Override
	public Set<Class<?>> supportedTypes()
	{
		return this.supportedTypes;
	}
}
