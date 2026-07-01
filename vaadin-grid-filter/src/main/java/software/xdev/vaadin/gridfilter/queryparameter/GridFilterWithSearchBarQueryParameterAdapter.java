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
