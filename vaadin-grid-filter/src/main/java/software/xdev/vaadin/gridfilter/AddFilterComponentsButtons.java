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

import java.util.Collection;
import java.util.function.Consumer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponentSupplier;


public class AddFilterComponentsButtons extends HorizontalLayout
{
	public void update(
		final Collection<FilterComponentSupplier> filterComponentSuppliers,
		final Consumer<FilterComponentSupplier> onAddButtonClicked)
	{
		this.removeAll();
		filterComponentSuppliers.stream()
			.map(s -> {
				final Button btn =
					new Button(s.display(), VaadinIcon.PLUS.create(), ev -> onAddButtonClicked.accept(s));
				btn.addThemeVariants(ButtonVariant.LUMO_SMALL);
				return btn;
			})
			.forEach(this::add);
	}
}
