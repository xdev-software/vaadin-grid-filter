package software.xdev.vaadin.gridfilter.demo;

import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.router.Route;

import software.xdev.vaadin.gridfilter.GridFilter;
import software.xdev.vaadin.gridfilter.model.Person;


@Route(MinimalisticDemo.NAV)
public class MinimalisticDemo extends AbstractDemo
{
	public static final String NAV = "/minimalistic";
	
	public MinimalisticDemo()
	{
		final GridFilter<Person> filter = this.createDefaultFilter();
		
		// Add filter inside details block for better looking UI
		final Details details = new Details("Filter data");
		details.addThemeVariants(DetailsVariant.FILLED);
		details.add(filter);
		details.setOpened(true);
		this.add(details, this.grid);
	}
}
