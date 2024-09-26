package software.xdev.vaadin.gridfilter;

import java.util.Collection;
import java.util.function.Consumer;

import com.vaadin.flow.component.button.Button;
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
			.map(s -> new Button(s.display(), VaadinIcon.PLUS.create(), ev -> onAddButtonClicked.accept(s)))
			.forEach(this::add);
	}
}
