package software.xdev.vaadin.gridfilter;

import java.util.function.BiConsumer;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponent;


public class FilterComponentWrapper<T> extends HorizontalLayout
{
	public FilterComponentWrapper(
		final boolean fullWidth,
		final FilterComponent<T, ?> component,
		final BiConsumer<FilterComponentWrapper<T>, FilterComponent<T, ?>> onRemove)
	{
		final Button btnDelete = new Button(VaadinIcon.TRASH.create(), ev -> onRemove.accept(this, component));
		btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		btnDelete.getStyle().set("margin-left", "var(--lumo-space-s)");
		
		this.add(component, btnDelete);
		
		this.setAlignItems(Alignment.CENTER);
		this.setSpacing(false);
		
		if(fullWidth)
		{
			this.setWidthFull();
			this.setJustifyContentMode(JustifyContentMode.BETWEEN);
		}
	}
}
