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
		this(gridFilter, queryParamKey, true);
	}
	
	public GridFilterQueryParameterAdapter(
		final GridFilter<?> gridFilter,
		final String queryParamKey,
		final boolean attachUpdateListener)
	{
		this.gridFilter = Objects.requireNonNull(gridFilter);
		this.queryParamKey = Objects.requireNonNull(queryParamKey);
		if(attachUpdateListener)
		{
			this.addQueryParamUpdateListener();
		}
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
