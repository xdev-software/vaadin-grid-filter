package software.xdev.vaadin.gridfilter.business.operation;

import software.xdev.vaadin.gridfilter.business.value.NoValue;


public class IsEmptyOp implements Operation<NoValue>
{
	@Override
	public Class<?> valueContainerClass()
	{
		return NoValue.class;
	}
	
	@Override
	public boolean canHandle(final Class<?> clazz)
	{
		return true;
	}
	
	@Override
	public String display()
	{
		return "is empty";
	}
	
	@Override
	public boolean test(final Object input, final NoValue filterValue)
	{
		return input == null || (input instanceof final String s && s.isEmpty());
	}
}
