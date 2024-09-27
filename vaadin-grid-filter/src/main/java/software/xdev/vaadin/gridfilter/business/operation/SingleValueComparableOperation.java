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
