package software.xdev.vaadin.gridfilter.queryparameter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.vaadin.flow.router.QueryParameters;

import software.xdev.vaadin.gridfilter.GridFilterWithSearchBar;


/**
 * Synchronizes the current filters with query parameters
 */
public class GridFilterWithSearchBarQueryParameterAdapter
	extends AbstractGridFilterQueryParameterAdapter<GridFilterWithSearchBar<?>>
{
	protected final String searchBarQueryParamKey;
	
	public GridFilterWithSearchBarQueryParameterAdapter(
		final GridFilterWithSearchBar<?> gridFilter,
		final String queryParamKey,
		final String searchBarQueryParamKey)
	{
		super(gridFilter, queryParamKey);
		this.searchBarQueryParamKey = Objects.requireNonNull(searchBarQueryParamKey);
	}
	
	public GridFilterWithSearchBarQueryParameterAdapter(
		final GridFilterWithSearchBar<?> gridFilter,
		final String queryParamKey,
		final String searchBarQueryParamKey,
		final boolean attachUpdateListener)
	{
		super(gridFilter, queryParamKey, attachUpdateListener);
		this.searchBarQueryParamKey = Objects.requireNonNull(searchBarQueryParamKey);
	}
	
	@Override
	protected void handleFilterChangedModifyQueryParameters(final Map<String, List<String>> parameters)
	{
		this.gridFilter.getSearchBarValue()
			.ifPresentOrElse(
				v -> parameters.put(this.searchBarQueryParamKey, List.of(v)),
				() -> parameters.remove(this.searchBarQueryParamKey)
			);
		
		super.handleFilterChangedModifyQueryParameters(parameters);
	}
	
	@Override
	public void loadFrom(final QueryParameters queryParameters)
	{
		this.gridFilter.setSearchBarValue(queryParameters.getParameters(this.searchBarQueryParamKey)
			.stream()
			.findFirst());
		
		super.loadFrom(queryParameters);
		
		this.gridFilter.showDetailsFilterComponentsAndHideExpandButtonIfRequired();
	}
}
