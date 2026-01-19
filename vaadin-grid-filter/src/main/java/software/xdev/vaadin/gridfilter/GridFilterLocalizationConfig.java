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
package software.xdev.vaadin.gridfilter;

import static java.util.Map.entry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import com.vaadin.flow.component.Component;


public class GridFilterLocalizationConfig
{
	public static final String PREFIX = "gridfilter.";
	
	public static final String PREFIX_OP = PREFIX + "op.";
	public static final String OP_EQUALS = PREFIX_OP + "equals";
	public static final String OP_CONTAINS = PREFIX_OP + "contains";
	public static final String OP_GREATER_THAN = PREFIX_OP + "gt";
	public static final String OP_LESS_THAN = PREFIX_OP + "lt";
	public static final String OP_IS_EMPTY = PREFIX_OP + "empty";
	
	public static final String PREFIX_BLOCK = PREFIX + "block.";
	public static final String BLOCK_OR = PREFIX_BLOCK + "or";
	public static final String BLOCK_AND = PREFIX_BLOCK + "and";
	public static final String BLOCK_NOT = PREFIX_BLOCK + "not";
	public static final String CONDITION = PREFIX + "condition";
	
	// Key, Default Value
	public static final Map<String, String> DEFAULT_VALUES = Map.ofEntries(
		// Operation
		entry(OP_EQUALS, "="),
		entry(OP_CONTAINS, "contains"),
		entry(OP_GREATER_THAN, ">"),
		entry(OP_LESS_THAN, "<"),
		entry(OP_IS_EMPTY, "is empty"),
		// Block
		entry(BLOCK_OR, "OR"),
		entry(BLOCK_AND, "AND"),
		entry(BLOCK_NOT, "NOT"),
		entry(CONDITION, "condition")
	);
	
	// Key, Resolver
	protected final Map<String, BiFunction<Component, String, String>> keyResolvers = new HashMap<>();
	
	public GridFilterLocalizationConfig withAll(final Map<String, String> keyValues)
	{
		keyValues.forEach(this::with);
		return this;
	}
	
	public GridFilterLocalizationConfig with(final String key, final String value)
	{
		this.getKeyResolvers().put(key, (c, k) -> value);
		return this;
	}
	
	public GridFilterLocalizationConfig withTranslation(final String key, final String i18nKey)
	{
		this.getKeyResolvers().put(
			key,
			(c, k) -> c.getTranslation(i18nKey));
		return this;
	}
	
	public String getTranslation(final String key, final Component caller)
	{
		final BiFunction<Component, String, String> resolver = this.getKeyResolvers().get(key);
		if(resolver == null)
		{
			final String defaultValue = DEFAULT_VALUES.get(key);
			return defaultValue != null ? defaultValue : key;
		}
		return resolver.apply(caller, key);
	}
	
	public Map<String, BiFunction<Component, String, String>> getKeyResolvers()
	{
		return this.keyResolvers;
	}
}
