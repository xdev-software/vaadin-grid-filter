package software.xdev.vaadin.gridfilter.filtercomponents;

import java.util.function.Predicate;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;


public abstract class FilterComponent<T, C extends Component>
	extends Composite<C>
	implements Predicate<T>
{
}
