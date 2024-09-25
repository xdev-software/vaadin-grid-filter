package software.xdev.vaadin.gridfilter.business.operation;

import software.xdev.vaadin.gridfilter.business.value.SingleValue;


public class ContainsOp extends SingleValueOperation<String>
{
	public ContainsOp()
	{
		super(String.class, "contains");
	}
	
	@Override
	public boolean testTyped(final String input, final SingleValue<String> filterValue)
	{
		if(input == null)
		{
			return false;
		}
		return input.contains(filterValue.getValue());
	}
}
