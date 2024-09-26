package software.xdev.vaadin.gridfilter;

import java.util.function.Function;


public record FilterableField<I, T>(
	String name,
	Function<I, T> keyExtractor,
	Class<T> clazz)
{
	public String identifier()
	{
		return this.name().chars()
			.filter(c -> Character.isLetter(c) || Character.isDigit(c))
			.collect(
				() -> new StringBuilder(this.name().length()),
				StringBuilder::appendCodePoint,
				StringBuilder::append)
			.toString();
	}
}
