package software.xdev.vaadin.gridfilter.queryparameter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;

import software.xdev.vaadin.gridfilter.GridFilter;


public class GridFilterQueryParameterAdapter
{
	protected final GridFilter<?> gridFilter;
	protected final String queryParamKey;
	
	public GridFilterQueryParameterAdapter(final GridFilter<?> gridFilter, final String queryParamKey)
	{
		this.gridFilter = Objects.requireNonNull(gridFilter);
		this.queryParamKey = Objects.requireNonNull(queryParamKey);
		this.addQueryParamUpdateListener();
	}
	
	protected void addQueryParamUpdateListener()
	{
		this.gridFilter.addFilterChangedListener(ev -> {
			final UI currentUI = UI.getCurrent();
			final Location currentLocation = currentUI.getActiveViewLocation();
			
			final Map<String, List<String>> parameters =
				new LinkedHashMap<>(currentLocation.getQueryParameters().getParameters());
			Optional.ofNullable(this.gridFilter.serialize())
				.filter(s -> !s.isEmpty())
				.ifPresentOrElse(
					qpValue -> parameters.put(this.queryParamKey, List.of(qpValue)),
					() -> parameters.remove(this.queryParamKey));
			
			currentUI.getPage().getHistory().replaceState(
				null,
				new Location(
					currentLocation.getPath(),
					new QueryParameters(parameters)));
		});
	}
	
	public void afterNavigation(final AfterNavigationEvent event)
	{
		this.loadFrom(event.getLocation().getQueryParameters());
	}
	
	public void loadFrom(final QueryParameters queryParameters)
	{
		queryParameters.getParameters(this.queryParamKey)
			.stream()
			.findFirst()
			.ifPresent(this.gridFilter::deserializeAndApply);
	}
}
