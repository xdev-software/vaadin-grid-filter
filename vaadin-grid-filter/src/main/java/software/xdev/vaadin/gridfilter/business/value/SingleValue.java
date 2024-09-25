package software.xdev.vaadin.gridfilter.business.value;

public class SingleValue<V> implements ValueContainer
{
	protected V value;
	
	public V getValue()
	{
		return this.value;
	}
	
	public void setValue(final V value)
	{
		this.value = value;
	}
	
	@SuppressWarnings("unchecked")
	public void setValueUnchecked(final Object value)
	{
		this.setValue((V)value);
	}
	
	@Override
	public boolean isValid()
	{
		return this.value != null;
	}
}
