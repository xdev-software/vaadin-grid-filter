package software.xdev.vaadin.gridfilter.model;

import java.time.LocalDate;
import java.util.List;

import org.jspecify.annotations.Nullable;


public record Person(
	Integer id,
	String firstName,
	String lastName,
	LocalDate birthday,
	boolean married,
	double salary,
	Department department,
	@Nullable
	City city
)
{
	@SuppressWarnings("checkstyle:MagicNumber")
	public static List<Person> list()
	{
		return List.of(
			new Person(
				1,
				"Siegbert",
				"Schmidt",
				LocalDate.of(1990, 12, 17),
				false,
				1000,
				Department.HR,
				City.AMSTERDAM),
			new Person(2, "Herbert", "Maier", LocalDate.of(1967, 10, 13), false, 1000, Department.HR, City.AMSTERDAM),
			new Person(3, "Hans", "Lang", LocalDate.of(2002, 5, 9), true, 9050.60, Department.HR, City.AMSTERDAM),
			new Person(4, "Anton", "Meier", LocalDate.of(1985, 1, 24), true, 8000.75, Department.HR, City.NEW_YORK),
			new Person(5, "Sarah", "Smith", LocalDate.of(1999, 6, 1), false, 5000, Department.IT, City.BERLIN),
			new Person(6, "Niklas", "Sommer", LocalDate.of(1994, 11, 8), true, 4000.33, Department.HR, City.BERLIN),
			new Person(7, "Hanna", "Neubaum", LocalDate.of(1986, 8, 15), true, 3000, Department.HR, null),
			new Person(8, "Laura", "Fels", LocalDate.of(1996, 3, 20), true, 1000.50, Department.HR, null),
			new Person(9, "Sofia", "Sommer", LocalDate.of(1972, 4, 14), false, 2000, Department.ACCOUNTING, null)
		);
	}
}
