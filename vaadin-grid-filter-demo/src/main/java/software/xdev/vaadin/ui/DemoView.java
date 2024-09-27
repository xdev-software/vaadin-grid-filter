package software.xdev.vaadin.ui;

import java.time.LocalDate;

import com.vaadin.flow.component.AttachEvent;
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
			.withFilterableField("id", Person::id, Integer.class)
			.withFilterableField("firstName", Person::firstName, String.class)
			.withFilterableField("birthday", Person::birthday, LocalDate.class)
			.withFilterableField("married", Person::married, Boolean.class)
			.withFilterableField("department", Person::department, Department.class);
		this.add(filter, this.grid);
		
		this.queryParameterAdapter = new GridFilterQueryParameterAdapter(filter, "filter");
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
