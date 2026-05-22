package software.xdev.vaadin.gridfilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.jspecify.annotations.NonNull;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;


public class GridFilterWithSearchBar<T> extends GridFilter<T>
{
	protected final TextField txtSearchBar = new TextField();
	
	protected final HorizontalLayout hlSearchBar = new HorizontalLayout();
	protected Button btnExpandDetailSearchComponents;
	protected final List<Function<T, String>> searchBarValueExtractors = new ArrayList<>();
	
	public GridFilterWithSearchBar(final Grid<T> grid)
	{
		this(grid, true);
	}
	
	protected GridFilterWithSearchBar(final Grid<T> grid, final boolean hideDetailsFilterInitially)
	{
		this(grid, hideDetailsFilterInitially, true);
	}
	
	protected GridFilterWithSearchBar(
		final Grid<T> grid,
		final boolean hideDetailsFilterInitially,
		final boolean initUI)
	{
		super(grid, false);
		
		this.withApplyFilter((gr, components) -> {
			
			final String searchBarValue = this.txtSearchBar.getValue();
			final boolean hasSearchBarValue = searchBarValue != null && !searchBarValue.isEmpty();
			
			gr.getListDataView().setFilter(item ->
				(!hasSearchBarValue || this.doesItemMatchAnySearchBarExtracted(item, searchBarValue))
					&& components.stream().allMatch(c -> c.test(item)));
		});
		
		if(initUI)
		{
			this.initUIWithSearchBar(hideDetailsFilterInitially);
		}
	}
	
	protected void initUIWithSearchBar(final boolean hideDetailsFilterInitially)
	{
		this.initSearchBar(hideDetailsFilterInitially);
		
		super.initUI();
		
		if(hideDetailsFilterInitially)
		{
			this.setDetailsFilterComponentsVisible(false);
		}
	}
	
	protected void initSearchBar(final boolean hideDetailsFilterInitially)
	{
		this.txtSearchBar.setPlaceholder("Search...");
		this.txtSearchBar.setWidthFull();
		this.txtSearchBar.addValueChangeListener(ev -> this.onFilterUpdate());
		
		this.hlSearchBar.add(this.txtSearchBar);
		
		if(hideDetailsFilterInitially)
		{
			this.btnExpandDetailSearchComponents = new Button(
				VaadinIcon.FILTER.create(),
				ev -> this.showDetailsFilterComponentsAndHideExpandButton());
			this.btnExpandDetailSearchComponents.setDisableOnClick(true);
			this.btnExpandDetailSearchComponents.setTooltipText("Show detailed filters");
			this.hlSearchBar.add(this.btnExpandDetailSearchComponents);
		}
		
		this.hlSearchBar.setWidthFull();
		
		this.add(this.hlSearchBar);
	}
	
	protected void setDetailsFilterComponentsVisible(final boolean visible)
	{
		this.filterContainerComponent.setVisible(visible);
		this.addFilterComponentButtons.setVisible(visible);
	}
	
	protected boolean doesItemMatchAnySearchBarExtracted(final T item, @NonNull final String value)
	{
		return this.searchBarValueExtractors.stream()
			.map(func -> func.apply(item))
			.filter(Objects::nonNull)
			.anyMatch(s -> s.contains(value));
	}
	
	@SuppressWarnings({"unchecked", "varargs"})
	public GridFilterWithSearchBar<T> withSearchBarValueExtractors(final Function<T, String>... extractors)
	{
		return this.withSearchBarValueExtractors(List.of(extractors));
	}
	
	public GridFilterWithSearchBar<T> withSearchBarValueExtractors(final Collection<Function<T, String>> extractors)
	{
		this.searchBarValueExtractors.addAll(extractors);
		return this;
	}
	
	public GridFilterWithSearchBar<T> withSearchBarValueExtractor(final Function<T, String> extractor)
	{
		this.searchBarValueExtractors.add(extractor);
		return this;
	}
	
	public Optional<String> getSearchBarValue()
	{
		return Optional.ofNullable(this.txtSearchBar.getValue())
			.filter(value -> !Objects.equals(value, this.txtSearchBar.getEmptyValue()));
	}
	
	public void setSearchBarValue(final Optional<String> optValue)
	{
		this.txtSearchBar.setValue(optValue.orElseGet(this.txtSearchBar::getEmptyValue));
	}
	
	protected void showDetailsFilterComponentsAndHideExpandButton()
	{
		this.hlSearchBar.remove(this.btnExpandDetailSearchComponents);
		this.btnExpandDetailSearchComponents = null;
		
		this.setDetailsFilterComponentsVisible(true);
	}
	
	public void showDetailsFilterComponentsAndHideExpandButtonIfRequired()
	{
		if(this.btnExpandDetailSearchComponents != null
			&& !this.filterContainerComponent.getFilterComponents().isEmpty())
		{
			this.showDetailsFilterComponentsAndHideExpandButton();
		}
	}
	
	public static <T> GridFilterWithSearchBar<T> createDefault(final Grid<T> grid)
	{
		return applyDefault(new GridFilterWithSearchBar<>(grid));
	}
}
