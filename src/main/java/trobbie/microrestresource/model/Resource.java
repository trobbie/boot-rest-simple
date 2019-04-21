package trobbie.microrestresource.model;

/**
 * @author Trevor Robbie
 *
 */
public class Resource {

	private final long id;
	private final String name;

	public Resource(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}

