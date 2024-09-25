package software.xdev.vaadin.gridfilter;

import java.util.function.Function;


public record FilterableField<I, T>(
	String name,
	Function<I, T> keyExtractor,
	Class<T> clazz)
{
}
