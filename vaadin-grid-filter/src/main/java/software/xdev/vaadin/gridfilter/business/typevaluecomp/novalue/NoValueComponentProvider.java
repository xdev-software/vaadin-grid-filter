package software.xdev.vaadin.gridfilter.business.typevaluecomp.novalue;

import java.util.Set;

import com.vaadin.flow.data.binder.Binder;

import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentData;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.TypeValueComponentProvider;
import software.xdev.vaadin.gridfilter.business.value.NoValue;


public class NoValueComponentProvider implements TypeValueComponentProvider<NoValue>
{
	@Override
	public Class<?> valueContainerClass()
	{
		return NoValue.class;
	}
	
	@Override
	public NoValue createEmptyValueContainer()
	{
		return new NoValue();
	}
	
	@Override
	public Set<Class<?>> supportedTypes()
	{
		return Set.of(Object.class);
	}
	
	@Override
	public boolean canHandle(final Class<?> clazz)
	{
		return true;
	}
	
	@Override
	public TypeValueComponentData<NoValue> getNewComponentData(final Class<?> clazz)
	{
		return new TypeValueComponentData<>(new Binder<>(), null);
	}
}
