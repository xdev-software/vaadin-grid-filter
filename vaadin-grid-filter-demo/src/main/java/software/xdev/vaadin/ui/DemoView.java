package software.xdev.vaadin.ui;

import java.time.LocalDate;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

import software.xdev.vaadin.gridfilter.GridFilter;
import software.xdev.vaadin.gridfilter.queryparameter.GridFilterQueryParameterAdapter;
import software.xdev.vaadin.model.Department;
import software.xdev.vaadin.model.Person;


@Route("")
public class DemoView extends VerticalLayout implements AfterNavigationObserver
{
	private final Grid<Person> grid = new Grid<>(Person.class, true);
	private final GridFilterQueryParameterAdapter queryParameterAdapter;
	
	public DemoView()
	{
		final GridFilter<Person> filter = GridFilter.createDefault(this.grid)
			.withFilterableField("ID", Person::id, Integer.class)
			.withFilterableField("First Name", Person::firstName, String.class)
			.withFilterableField("Birthday", Person::birthday, LocalDate.class)
			.withFilterableField("Married", Person::married, Boolean.class)
			.withFilterableField("Department", Person::department, Department.class)
			.withMaxNestedDepth(5);
		
		final Details details = new Details("Filter data");
		details.addThemeVariants(DetailsVariant.FILLED);
		details.add(filter);
		details.setOpened(true);
		this.add(details, this.grid);
		
		this.queryParameterAdapter = new GridFilterQueryParameterAdapter(filter, "filter");
		
		this.setSpacing(false);
	}
	
	@Override
	protected void onAttach(final AttachEvent attachEvent)
	{
		this.grid.setItems(Person.list());
	}
	
	@Override
	public void afterNavigation(final AfterNavigationEvent event)
	{
		this.queryParameterAdapter.afterNavigation(event);
	}
}
