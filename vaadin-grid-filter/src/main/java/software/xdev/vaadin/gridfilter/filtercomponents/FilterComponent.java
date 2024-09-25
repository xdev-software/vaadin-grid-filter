package software.xdev.vaadin.gridfilter.filtercomponents;

import java.util.function.Predicate;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;


public abstract class FilterComponent<T> extends HorizontalLayout implements Predicate<T>
{
}
