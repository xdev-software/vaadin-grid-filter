/*
 * Copyright © 2024 XDEV Software (https://xdev.software)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
