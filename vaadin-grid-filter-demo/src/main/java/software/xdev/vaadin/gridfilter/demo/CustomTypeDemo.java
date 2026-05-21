package software.xdev.vaadin.gridfilter.demo;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.router.Route;

import software.xdev.vaadin.gridfilter.GridFilter;
import software.xdev.vaadin.gridfilter.business.typevaluecomp.single.SingleValueComponentProvider;
import software.xdev.vaadin.gridfilter.model.City;
import software.xdev.vaadin.gridfilter.model.Person;


@Route(CustomTypeDemo.NAV)
public class CustomTypeDemo extends AbstractDemo
{
	public static final String NAV = "/customtype";
	
	public CustomTypeDemo()
	{
		final GridFilter<Person> filter = this.createDefaultFilter()
			.addTypeValueComponent(new SingleValueComponentProvider<>(
				City.class,
				() -> {
					final ComboBox<City> comboBox = new ComboBox<>();
					comboBox.setItemLabelGenerator(City::name);
					comboBox.setItems(City.allAvailable().values());
					return comboBox;
				},
				City::name,
				name -> City.allAvailable().get(name)
			))
			.withFilterableField("City", Person::city, City.class);
		
		// Add filter inside details block for better looking UI
		final Details details = new Details("Filter data");
		details.addThemeVariants(DetailsVariant.LUMO_FILLED);
		details.add(filter);
		details.setOpened(true);
		this.add(details, this.grid);
	}
}
