package software.xdev.vaadin.gridfilter.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public record City(
	String name
)
{
	public static final City AMSTERDAM = new City("Amsterdam");
	public static final City BERLIN = new City("Berlin");
	public static final City NEW_YORK = new City("New York");
	
	public static Map<String, City> allAvailable()
	{
		return Stream.of(AMSTERDAM, BERLIN, NEW_YORK)
			.collect(Collectors.toMap(
				City::name,
				Function.identity(),
				(l, r) -> l,
				LinkedHashMap::new));
	}
}
