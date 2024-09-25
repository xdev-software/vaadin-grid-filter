package software.xdev.vaadin.gridfilter.business.operation;

import software.xdev.vaadin.gridfilter.business.value.ValueContainer;


public interface Operation<V extends ValueContainer>
{
	Class<?> valueContainerClass();
	
	boolean canHandle(Class<?> clazz);
	
	String display();
	
	boolean test(Object input, V filterValue);
	
	@SuppressWarnings("unchecked")
	default boolean testUnchcked(final Object input, final Object filterValue)
	{
		return this.test(input, (V)filterValue);
	}
}
