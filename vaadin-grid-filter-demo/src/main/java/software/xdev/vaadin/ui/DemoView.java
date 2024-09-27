package software.xdev.vaadin.ui;

import java.time.LocalDate;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
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
			.withSearchableField("id", Person::id, Integer.class)
			.withSearchableField("firstName", Person::firstName, String.class)
			.withSearchableField("birthday", Person::birthday, LocalDate.class)
			.withSearchableField("married", Person::married, Boolean.class)
			.withSearchableField("department", Person::department, Department.class);
		this.add(filter);
		
		this.queryParameterAdapter = new GridFilterQueryParameterAdapter(filter, "filter");
		
		this.add(new Button("Serialize", ev -> System.out.println(filter.serialize())));
		this.add(new Button(
			"Fill in",
			ev -> filter.deserializeAndApply(
				"_OR(id = 1,_AND(birthday = 2024-09-26,firstName contains %2C%29%28%3D+)),id = 1")));
		this.add(this.grid);
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
