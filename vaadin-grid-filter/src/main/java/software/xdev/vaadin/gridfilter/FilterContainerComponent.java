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

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponent;


public class FilterContainerComponent<T> extends VerticalLayout
{
	protected final Runnable onValueUpdated;
	protected final boolean shouldWrapperUseFullWidth;
	
	protected final List<FilterComponent<T, ?>> filterComponents = new ArrayList<>();
	
	public FilterContainerComponent(final Runnable onValueUpdated, final boolean shouldWrapperUseFullWidth)
	{
		this.onValueUpdated = onValueUpdated;
		this.shouldWrapperUseFullWidth = shouldWrapperUseFullWidth;
		
		this.setPadding(false);
		this.setSpacing(false);
		
		this.updateFilterConditionsContainerVisibility();
	}
	
	public void addFilterComponent(final FilterComponent<T, ?> newFilterConditionComponent)
	{
		this.filterComponents.add(newFilterConditionComponent);
		this.updateFilterConditionsContainerVisibility();
		
		this.add(new FilterComponentWrapper<>(
			this.shouldWrapperUseFullWidth,
			newFilterConditionComponent,
			(self, c) -> {
				this.filterComponents.remove(c);
				this.remove(self);
				this.updateFilterConditionsContainerVisibility();
				
				this.onValueUpdated.run();
			}));
	}
	
	public void updateFilterConditionsContainerVisibility()
	{
		this.setVisible(!this.filterComponents.isEmpty());
	}
	
	public List<FilterComponent<T, ?>> getFilterComponents()
	{
		return this.filterComponents;
	}
}
