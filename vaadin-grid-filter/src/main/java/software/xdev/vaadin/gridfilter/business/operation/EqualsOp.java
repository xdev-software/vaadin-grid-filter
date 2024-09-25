package software.xdev.vaadin.gridfilter.business.operation;

import java.util.Objects;

import software.xdev.vaadin.gridfilter.business.value.SingleValue;


public class EqualsOp extends SingleValueOperation<Object>
{
	public EqualsOp()
	{
		super(Object.class, "=");
	}
	
	@Override
	public boolean canHandle(final Class<?> clazz)
	{
		return true;
	}
	
	@Override
	public boolean test(final Object input, final SingleValue<Object> filterValue)
	{
		return Objects.equals(input, filterValue.getValue());
	}
	
	@Override
	public boolean testTyped(final Object input, final SingleValue<Object> filterValue)
	{
		// No functionality
		return false;
	}
}
