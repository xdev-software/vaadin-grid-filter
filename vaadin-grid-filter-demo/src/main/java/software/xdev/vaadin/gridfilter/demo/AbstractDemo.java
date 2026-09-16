package software.xdev.vaadin.gridfilter.demo;

import java.time.LocalDate;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import software.xdev.vaadin.gridfilter.GridFilter;
import software.xdev.vaadin.gridfilter.model.Department;
import software.xdev.vaadin.gridfilter.model.Person;


public class AbstractDemo extends VerticalLayout
{
	protected final Grid<Person> grid = new Grid<>(Person.class, true);
	
	protected GridFilter<Person> createDefaultFilter()
	{
		return this.applyDefault(GridFilter.createDefault(this.grid));
	}
	
	protected <C extends GridFilter<Person>> C applyDefault(final C gridFilter)
	{
		gridFilter
			.withFilterableField("ID", Person::id, Integer.class)
			.withFilterableField("First Name", Person::firstName, String.class)
			.withFilterableField("Birthday", Person::birthday, LocalDate.class)
			.withFilterableField("Married", Person::married, Boolean.class)
			.withFilterableField("Department", Person::department, Department.class);
		return gridFilter;
	}
	
	@Override
	protected void onAttach(final AttachEvent attachEvent)
	{
		this.grid.setItems(Person.list());
	}
}
