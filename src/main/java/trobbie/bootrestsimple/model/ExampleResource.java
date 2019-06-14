package trobbie.bootrestsimple.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Implements a resource having ID's of Long type, and an additional Name field.
 *
 * @author Trevor Robbie
 *
 */
@Entity // designating a JPA entity (w/o @Table, assumed mapped to table of same name as class)
public class ExampleResource implements Resource<Long> {

	@Id // JPA can now recognize this as entity's ID
	@GeneratedValue
	private Long id;

	private String name;

	public ExampleResource() {
	}

	public ExampleResource(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * provides string format of the object's state
	 */
	@Override
	public String toString() {
		return String.format(
				"SimpleResource[id=%d, name='%s']",
				id, name);
	}

}

