package software.xdev.vaadin.gridfilter.business.operation;

@SuppressWarnings("rawtypes")
public abstract class SingleValueComparableOperation extends SingleValueOperation<Comparable>
{
	protected SingleValueComparableOperation(final String display)
	{
		super(Comparable.class, display);
	}
	
	@Override
	public boolean canHandle(final Class<?> clazz)
	{
		return super.canHandle(clazz)
			// Ignore nonsense compare types
			&& !String.class.equals(clazz)
			// Boolean has 2 values
			&& !Boolean.class.equals(clazz)
			// Enums are usually unique
			&& !Enum.class.isAssignableFrom(clazz);
	}
}
