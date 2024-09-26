package software.xdev.vaadin.gridfilter;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import software.xdev.vaadin.gridfilter.filtercomponents.FilterComponent;


public class FilterContainerComponent<T> extends VerticalLayout
{
	protected final Runnable onValueUpdated;
	
	protected final List<FilterComponent<T, ?>> filterComponents = new ArrayList<>();
	
	public FilterContainerComponent(final Runnable onValueUpdated)
	{
		this.onValueUpdated = onValueUpdated;
		
		this.setPadding(false);
		
		this.updateFilterConditionsContainerVisibility();
	}
	
	public void addFilterComponent(final FilterComponent<T, ?> newFilterConditionComponent)
	{
		this.filterComponents.add(newFilterConditionComponent);
		this.updateFilterConditionsContainerVisibility();
		
		this.add(new FilterComponentWrapper<>(
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
