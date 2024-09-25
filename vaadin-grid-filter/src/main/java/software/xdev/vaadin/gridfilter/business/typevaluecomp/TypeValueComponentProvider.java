package software.xdev.vaadin.gridfilter.business.typevaluecomp;

import java.util.Set;

import software.xdev.vaadin.gridfilter.business.value.ValueContainer;


public interface TypeValueComponentProvider<V extends ValueContainer>
{
	Class<?> valueContainerClass();
	
	V createEmptyValueContainer();
	
	default Set<Class<?>> supportedTypes()
	{
		return Set.of();
	}
	
	default boolean canHandle(final Class<?> clazz)
	{
		return this.supportedTypes().stream().anyMatch(supported -> supported.isAssignableFrom(clazz));
	}
	
	TypeValueComponentData<V> getNewComponentData(final Class<?> clazz);
	
	default TypeValueComponentData<V> getNewComponentDataWithDefaults(final Class<?> clazz)
	{
		final TypeValueComponentData<V> componentData = this.getNewComponentData(clazz);
		componentData.binder().setBean(this.createEmptyValueContainer());
		return componentData;
	}
}
