package software.xdev.vaadin.ui;

import java.time.LocalDate;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import software.xdev.vaadin.gridfilter.GridFilter;
import software.xdev.vaadin.model.Department;
import software.xdev.vaadin.model.Person;


@Route("")
public class DemoView extends VerticalLayout
{
	private final Grid<Person> grid = new Grid<>(Person.class, true);
	
	public DemoView()
	{
		this.add(GridFilter.createDefault(this.grid)
			.withSearchableField("id", Person::id, Integer.class)
			.withSearchableField("firstName", Person::firstName, String.class)
			.withSearchableField("birthday", Person::birthday, LocalDate.class)
			.withSearchableField("married", Person::married, Boolean.class)
			.withSearchableField("department", Person::department, Department.class));
		this.add(this.grid);
	}
	
	@Override
	protected void onAttach(final AttachEvent attachEvent)
	{
		this.grid.setItems(Person.list());
	}
}
