package software.xdev.vaadin.gridfilter;

import java.util.List;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import software.xdev.vaadin.gridfilter.demo.LocalizationDemo;
import software.xdev.vaadin.gridfilter.demo.MaxNestedDepthDemo;
import software.xdev.vaadin.gridfilter.demo.MinimalisticDemo;
import software.xdev.vaadin.gridfilter.demo.QueryParameterDemo;


@PageTitle("Grid Filter demos")
@Route("")
public class DemoView extends Composite<VerticalLayout>
{
	private final Grid<Example> grExamples = new Grid<>();
	
	public DemoView()
	{
		this.grExamples
			.addColumn(new ComponentRenderer<>(example -> {
				final Anchor anchor = new Anchor(example.route(), example.name());
				
				final Span spDesc = new Span(example.desc());
				spDesc.getStyle().set("font-size", "90%");
				spDesc.getStyle().set("white-space", "pre");
				
				final VerticalLayout vl = new VerticalLayout(anchor, spDesc);
				vl.setSpacing(false);
				return vl;
			}))
			.setHeader("Available demos");
		
		this.grExamples.setSizeFull();
		this.grExamples.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_BORDER);
		
		this.getContent().add(this.grExamples);
		this.getContent().setHeightFull();
	}
	
	@Override
	protected void onAttach(final AttachEvent attachEvent)
	{
		this.grExamples.setItems(List.of(
			new Example(
				MinimalisticDemo.NAV,
				"Minimalistic",
				"Showcasing the simplest form of using the component"
			),
			new Example(
				QueryParameterDemo.NAV,
				"Store filters in QueryParameter",
				"Shows how filters can be persisted in and loaded from QueryParameters/Url"
			),
			new Example(
				MaxNestedDepthDemo.NAV,
				"Limit depth/nesting of filters",
				"Limits the how many filters can be nested"
			),
			new Example(
				LocalizationDemo.NAV,
				"Localization",
				"Showcases how localization can be done (UI in German)"
			)
		));
	}
	
	record Example(String route, String name, String desc)
	{
	}
}
