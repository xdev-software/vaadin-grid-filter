package software.xdev.vaadin.gridfilter.filtercomponents;

/**
 * Serialization for FilterComponents - primarily designed for QueryParameters
 */
public interface FilterComponentSerialization
{
	String serialize();
	
	void deserializeAndApply(String input);
}
