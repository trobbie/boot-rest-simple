package trobbie.microrestresource.model;

/**
 * Implements a resource having ID's of Long type, and an additional Name field.
 *
 * @author Trevor Robbie
 *
 */
public class SimpleResource implements Resource<Long> {

	private Long id;
	private String name;

	public SimpleResource() {
	}

	public SimpleResource(Long id, String name) {
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


}

