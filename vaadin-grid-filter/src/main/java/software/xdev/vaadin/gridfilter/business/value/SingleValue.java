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
package software.xdev.vaadin.gridfilter.business.value;

public class SingleValue<V> implements ValueContainer
{
	protected V value;
	
	public V getValue()
	{
		return this.value;
	}
	
	public void setValue(final V value)
	{
		this.value = value;
	}
	
	@SuppressWarnings("unchecked")
	public void setValueUnchecked(final Object value)
	{
		this.setValue((V)value);
	}
	
	@Override
	public boolean isValid()
	{
		return this.value != null;
	}
}