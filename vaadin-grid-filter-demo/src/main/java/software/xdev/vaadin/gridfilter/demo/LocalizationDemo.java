package software.xdev.vaadin.gridfilter.demo;

import com.vaadin.flow.router.Route;

import software.xdev.vaadin.gridfilter.GridFilter;
import software.xdev.vaadin.gridfilter.GridFilterLocalizationConfig;
import software.xdev.vaadin.gridfilter.model.Person;


@Route(LocalizationDemo.NAV)
public class LocalizationDemo extends AbstractDemo
{
	public static final String NAV = "/localization";
	
	public LocalizationDemo()
	{
		final GridFilter<Person> filter = this.createDefaultFilter()
			.withLocalizationConfig(new GridFilterLocalizationConfig()
				.with(GridFilterLocalizationConfig.BLOCK_AND, "UND")
				.with(GridFilterLocalizationConfig.BLOCK_OR, "ODER")
				.with(GridFilterLocalizationConfig.BLOCK_NOT, "NICHT")
				.with(GridFilterLocalizationConfig.OP_CONTAINS, "enthält")
				.with(GridFilterLocalizationConfig.OP_GREATER_THAN, "größer als")
				.with(GridFilterLocalizationConfig.OP_LESS_THAN, "kleiner als")
				.with(GridFilterLocalizationConfig.OP_EQUALS, "ist gleich")
				.with(GridFilterLocalizationConfig.OP_IS_EMPTY, "ist leer")
				.with(GridFilterLocalizationConfig.CONDITION, "Bedingung"));
		
		this.add(filter, this.grid);
	}
}
