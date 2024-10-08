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
package software.xdev.vaadin.gridfilter;

import java.util.Objects;
import java.util.function.Function;


public record FilterableField<I, T>(
	String name,
	String identifier,
	Function<I, T> keyExtractor,
	Class<T> clazz)
{
	public FilterableField
	{
		Objects.requireNonNull(name);
		Objects.requireNonNull(identifier);
		Objects.requireNonNull(keyExtractor);
		Objects.requireNonNull(clazz);
		if(identifier.chars().anyMatch(c -> !Character.isLetter(c) && !Character.isDigit(c)))
		{
			throw new IllegalArgumentException("identifier needs to be alphanumeric");
		}
	}
}
