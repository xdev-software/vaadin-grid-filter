package software.xdev.vaadin.gridfilter.business.operation;

import software.xdev.vaadin.gridfilter.business.value.SingleValue;


@SuppressWarnings("rawtypes")
public class GreaterThanOp extends SingleValueComparableOperation
{
	public GreaterThanOp()
	{
		super(">");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean testTyped(final Comparable input, final SingleValue<Comparable> filterValue)
	{
		return filterValue.getValue().compareTo(input) < 0;
	}
}
