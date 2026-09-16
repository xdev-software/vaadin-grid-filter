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

import software.xdev.vaadin.gridfilter.GridFilterLocalizationConfig;
import software.xdev.vaadin.gridfilter.business.value.NoValue;


public class IsNotEmptyOp implements Operation<NoValue>
{
	@Override
	public Class<?> valueContainerClass()
	{
		return NoValue.class;
	}
	
	@Override
	public boolean canHandle(final Class<?> clazz)
	{
		return true;
	}
	
	@Override
	public String identifier()
	{
		return "is not empty";
	}
	
	@Override
	public String displayKey()
	{
		return GridFilterLocalizationConfig.OP_IS_NOT_EMPTY;
	}
	
	@Override
	public boolean test(final Object input, final NoValue filterValue)
	{
		if(input instanceof final String s)
		{
			return !s.isEmpty();
		}
		
		return input != null;
	}
}
