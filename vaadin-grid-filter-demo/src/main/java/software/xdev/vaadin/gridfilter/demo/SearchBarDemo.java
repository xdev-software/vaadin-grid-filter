package software.xdev.vaadin.gridfilter.demo;

import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

import software.xdev.vaadin.gridfilter.GridFilterWithSearchBar;
import software.xdev.vaadin.gridfilter.model.Person;
import software.xdev.vaadin.gridfilter.queryparameter.GridFilterWithSearchBarQueryParameterAdapter;


@Route(SearchBarDemo.NAV)
public class SearchBarDemo extends AbstractDemo implements AfterNavigationObserver
{
	public static final String NAV = "/searchbar";
	
	private final GridFilterWithSearchBarQueryParameterAdapter queryParameterAdapter;
	
	@SuppressWarnings("unchecked")
	public SearchBarDemo()
	{
		final GridFilterWithSearchBar<Person> filter =
			this.applyDefault(GridFilterWithSearchBar.createDefault(this.grid))
				.withSearchBarValueExtractors(
					Person::firstName,
					Person::lastName);
		
		this.queryParameterAdapter = new GridFilterWithSearchBarQueryParameterAdapter(
			filter, "filter", "search");
		
		this.add(filter, this.grid);
	}
	
	@Override
	public void afterNavigation(final AfterNavigationEvent event)
	{
		this.queryParameterAdapter.afterNavigation(event);
	}
}
