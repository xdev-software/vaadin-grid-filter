package software.xdev.vaadin.gridfilter.business.value.reuse;

import software.xdev.vaadin.gridfilter.business.value.SingleValue;
import software.xdev.vaadin.gridfilter.business.value.ValueContainer;


@SuppressWarnings("rawtypes")
public class SingleValueReUseAdapter implements ValueReUseAdapter<SingleValue>
{
	@Override
	public Class<SingleValue> newValueClass()
	{
		return SingleValue.class;
	}
	
	@Override
	public boolean tryReUse(final ValueContainer prevValueContainer, final SingleValue newValueContainer)
	{
		if(prevValueContainer instanceof final SingleValue<?> prevSingleValue)
		{
			try
			{
				newValueContainer.setValueUnchecked(prevSingleValue.getValue());
				return true;
			}
			catch(final ClassCastException cce)
			{
				// type mismatched - can't copy over value ¯\_(ツ)_/¯
			}
		}
		return false;
	}
}
