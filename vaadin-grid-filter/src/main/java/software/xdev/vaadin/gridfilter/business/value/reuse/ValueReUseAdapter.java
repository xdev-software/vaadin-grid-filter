package software.xdev.vaadin.gridfilter.business.value.reuse;

import software.xdev.vaadin.gridfilter.business.value.ValueContainer;


public interface ValueReUseAdapter<N extends ValueContainer>
{
	Class<N> newValueClass();
	
	boolean tryReUse(ValueContainer prevValueContainer, N newValueContainer);
	
	@SuppressWarnings("unchecked")
	default boolean tryReUseUnchecked(final ValueContainer prevValueContainer, final Object newValueContainer)
	{
		return this.tryReUse(prevValueContainer, (N)newValueContainer);
	}
}
