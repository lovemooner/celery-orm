package com.oracle.demo.abdera.collectionAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.oracle.demo.model.Employee;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.i18n.text.Sanitizer;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.Person;
import org.apache.abdera.protocol.server.RequestContext;
import org.apache.abdera.protocol.server.context.ResponseContextException;
import org.apache.abdera.protocol.server.impl.AbstractEntityCollectionAdapter;

public class SimpleAdapter extends AbstractEntityCollectionAdapter<Employee> {

	private HashMap<Integer, Employee> employees = new HashMap<Integer, Employee>();

	{
		Employee employee = new Employee();
		employee.setId(1);
		employee.setName("Lily");
		employees.put(1, employee);
	}

	public String getId(RequestContext arg0) {
		return "tag:oracle.com,2007:employee2:feed";
	}

	public String getTitle(RequestContext arg0) {
		return "Simple Employee Database";
	}

	@Override
	public String getAuthor(RequestContext arg0) throws ResponseContextException {
		return "Simple author";
	}

	@Override
	public void deleteEntry(String resourceName, RequestContext request) throws ResponseContextException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getContent(Employee entry, RequestContext request) throws ResponseContextException {
		return "content";
	}

	@Override
	public Iterable<Employee> getEntries(RequestContext request) throws ResponseContextException {
		return employees.values();
	}

	@Override
	public Employee getEntry(String resourceName, RequestContext request) throws ResponseContextException {
		Integer id = getIdFromResourceName(resourceName);
		return employees.get(id);
	}

	private Integer getIdFromResourceName(String resourceName) throws ResponseContextException {
		int idx = resourceName.indexOf("-");
		if (idx == -1) {
			throw new ResponseContextException(404);
		}
		return new Integer(resourceName.substring(0, idx));
	}

	@Override
	public String getId(Employee entry) throws ResponseContextException {
		return String.valueOf(entry.getId());
	}

	@Override
	public String getName(Employee entry) throws ResponseContextException {
		return entry.getId() + "-" + Sanitizer.sanitize(entry.getName());
	}

	@Override
	public String getTitle(Employee entry) throws ResponseContextException {
		// TODO Auto-generated method stub
		return entry.getName();
	}

	@Override
	public Date getUpdated(Employee entry) throws ResponseContextException {
		return entry.getUpdated();
	}

	@Override
	public void putEntry(Employee entry, String title, Date updated, List<Person> authors, String summary,
			Content content, RequestContext request) throws ResponseContextException {
		// TODO Auto-generated method stub

	}

	@Override
	public Employee postEntry(String title, IRI id, String summary, Date updated, List<Person> authors, Content content,
			RequestContext request) throws ResponseContextException {
		return employees.get(1);
	}

}
