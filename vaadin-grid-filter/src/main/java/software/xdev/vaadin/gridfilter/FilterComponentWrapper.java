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

import java.util.function.BiConsumer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponent;


@CssImport(GridFilterStyles.LOCATION)
public class FilterComponentWrapper<T> extends HorizontalLayout
{
	public FilterComponentWrapper(
		final boolean fullWidth,
		final FilterComponent<T, ?> component,
		final BiConsumer<FilterComponentWrapper<T>, FilterComponent<T, ?>> onRemove)
	{
		final Button btnDelete = new Button(VaadinIcon.TRASH.create(), ev -> onRemove.accept(this, component));
		btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		btnDelete.addClassNames(GridFilterStyles.FILTER_COMPONENT_WRAPPER_BTN_DELETE);
		
		this.add(component, btnDelete);
		
		this.setAlignItems(Alignment.CENTER);
		this.setSpacing(false);
		this.addClassNames(GridFilterStyles.FILTER_COMPONENT_WRAPPER);
		
		if(fullWidth)
		{
			this.setWidthFull();
			this.setJustifyContentMode(JustifyContentMode.BETWEEN);
		}
	}
}
