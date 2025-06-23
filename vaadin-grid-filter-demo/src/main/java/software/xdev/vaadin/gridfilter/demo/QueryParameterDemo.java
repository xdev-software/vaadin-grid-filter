package software.xdev.vaadin.gridfilter.demo;

import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

import software.xdev.vaadin.gridfilter.GridFilter;
import software.xdev.vaadin.gridfilter.model.Person;
import software.xdev.vaadin.gridfilter.queryparameter.GridFilterQueryParameterAdapter;


@SuppressWarnings("java:S1948")
@Route(QueryParameterDemo.NAV)
public class QueryParameterDemo extends AbstractDemo implements AfterNavigationObserver
{
	public static final String NAV = "/queryparameter";
	
	private final GridFilterQueryParameterAdapter queryParameterAdapter;
	
	public QueryParameterDemo()
	{
		final GridFilter<Person> filter = this.createDefaultFilter();
		
		this.queryParameterAdapter = new GridFilterQueryParameterAdapter(filter, "filter");
		
		this.add(filter, this.grid);
	}
	
	@Override
	public void afterNavigation(final AfterNavigationEvent event)
	{
		this.queryParameterAdapter.afterNavigation(event);
	}
}
