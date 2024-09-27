package software.xdev.vaadin.gridfilter.filtercomponents;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


public final class FilterBlockComponentSerialization
{
	public static final char LIST_START = '(';
	public static final char LIST_SEPARATOR = ',';
	public static final char LIST_END = ')';
	
	public static <T> String serializeFilterComponents(final List<FilterComponent<T, ?>> filterComponents)
	{
		return filterComponents.stream()
			.map(FilterComponent::serialize)
			.filter(Objects::nonNull)
			.collect(Collectors.joining(String.valueOf(LIST_SEPARATOR)));
	}
	
	public static <T> void deserializeFilterComponents(
		final List<String> subElements,
		final List<FilterComponentSupplier> filterComponentSuppliers,
		final Function<FilterComponentSupplier, FilterComponent<T, ?>> addFilterComponent)
	{
		if(subElements.isEmpty())
		{
			return;
		}
		
		final List<FilterComponentSupplier> filterComponentSuppliersSorted = filterComponentSuppliers.stream()
			.sorted(Comparator.comparing(f -> -f.serializationPrefix().length()))
			.toList();
		
		subElements.forEach(sub -> filterComponentSuppliersSorted.stream()
			.filter(s -> sub.startsWith(s.serializationPrefix()))
			.findFirst()
			.map(addFilterComponent)
			.ifPresent(fc -> fc.deserializeAndApply(sub)));
	}
	
	public static List<String> deserializeSubElements(final String content)
	{
		final List<String> subElements = new ArrayList<>();
		
		int bracketCounter = 0;
		StringBuilder currentElementBuilder = new StringBuilder(content.length());
		int currentIndex = 0;
		for(final char c : content.toCharArray())
		{
			if(c == LIST_START)
			{
				bracketCounter++;
			}
			else if(c == LIST_END)
			{
				bracketCounter--;
				if(bracketCounter < 0)
				{
					// Invalid amount of brackets!
					return List.of();
				}
			}
			
			if(bracketCounter == 0 && c == LIST_SEPARATOR)
			{
				subElements.add(currentElementBuilder.toString());
				currentElementBuilder = new StringBuilder(content.length() - currentIndex);
			}
			else
			{
				currentElementBuilder.append(c);
			}
			currentIndex++;
		}
		
		if(bracketCounter > 0)
		{
			// Invalid amount of brackets!
			return List.of();
		}
		
		subElements.add(currentElementBuilder.toString());
		
		return subElements;
	}
	
	private FilterBlockComponentSerialization()
	{
	}
}
