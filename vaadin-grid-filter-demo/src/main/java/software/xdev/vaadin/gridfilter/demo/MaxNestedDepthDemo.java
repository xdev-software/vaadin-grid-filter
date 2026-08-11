package software.xdev.vaadin.gridfilter.demo;

import com.vaadin.flow.router.Route;

import software.xdev.vaadin.gridfilter.GridFilter;
import software.xdev.vaadin.gridfilter.model.Person;


@Route(MaxNestedDepthDemo.NAV)
public class MaxNestedDepthDemo extends AbstractDemo
{
	public static final String NAV = "/maxNestedDepth";
	
	public MaxNestedDepthDemo()
	{
		final GridFilter<Person> filter = this.createDefaultFilter()
			.withMaxNestedDepth(1);
		
		this.add(filter, this.grid);
	}
}
