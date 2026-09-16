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
package software.xdev.vaadin.gridfilter.business.operation;

import software.xdev.vaadin.gridfilter.business.value.ValueContainer;


/**
 * Defines a filter operation that can be applied on a value.
 * <p>
 * Examples include {@code =}, {@code <}, {@code >}, is empty, ...
 * </p>
 */
public interface Operation<V extends ValueContainer>
{
	/**
	 * Class of the {@link ValueContainer}
	 */
	Class<?> valueContainerClass();
	
	/**
	 * Checks if the value-class is supported.
	 * <p>
	 * inputs can be String.class, Double.class and so on
	 * </p>
	 */
	boolean canHandle(Class<?> clazz);
	
	String identifier();
	
	String displayKey();
	
	boolean test(Object input, V filterValue);
	
	@SuppressWarnings("unchecked")
	default boolean testUnchecked(final Object input, final Object filterValue)
	{
		return this.test(input, (V)filterValue);
	}
}
