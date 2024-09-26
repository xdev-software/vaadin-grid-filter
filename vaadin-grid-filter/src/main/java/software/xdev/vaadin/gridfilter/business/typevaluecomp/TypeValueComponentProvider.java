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
	
	String serialize(TypeValueComponentData<V> typeValueComponentData);
	
	@SuppressWarnings("unchecked")
	default String serializeUnchecked(final TypeValueComponentData<?> typeValueComponentData)
	{
		return this.serialize((TypeValueComponentData<V>)typeValueComponentData);
	}
	
	void deserializeAndApply(String input, TypeValueComponentData<V> typeValueComponentData);
	
	@SuppressWarnings("unchecked")
	default void deserializeAndApplyUnchecked(
		final String input,
		final TypeValueComponentData<?> typeValueComponentData)
	{
		this.deserializeAndApply(input, (TypeValueComponentData<V>)typeValueComponentData);
	}
}
