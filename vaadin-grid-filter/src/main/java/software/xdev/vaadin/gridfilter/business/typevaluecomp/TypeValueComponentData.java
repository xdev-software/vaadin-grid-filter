package software.xdev.vaadin.gridfilter.business.typevaluecomp;

import java.util.Objects;

import jakarta.annotation.Nullable;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;


public record TypeValueComponentData<V>(
	Binder<V> binder,
	@Nullable
	Component component
)
{
	public TypeValueComponentData
	{
		Objects.requireNonNull(binder);
	}
}
