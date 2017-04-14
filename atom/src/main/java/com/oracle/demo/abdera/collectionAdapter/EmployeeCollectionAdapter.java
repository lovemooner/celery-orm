package com.oracle.demo.abdera.collectionAdapter;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.Person;
import org.apache.abdera.model.Text;
import org.apache.abdera.model.Workspace;
import org.apache.abdera.protocol.server.RequestContext;
import org.apache.abdera.protocol.server.context.ResponseContextException;
import org.apache.abdera.protocol.server.impl.AbstractEntityCollectionAdapter;

public final class EmployeeCollectionAdapter extends AbstractEntityCollectionAdapter<Employee> {

    public static final String ID_PREFIX = "id:";
    private AtomicInteger nextId = new AtomicInteger(1000);
    private Factory factory = new Abdera().getFactory();

    private HashMap<Integer, Employee> employees = new HashMap<Integer, Employee>();

    {
        Employee employee = new Employee();
//        employee.setId(nextId.getAndIncrement());
//        employee.setName("init_name");
//        employees.put(employee.getId(), employee);
    }

    @Override
    public String getId(RequestContext request) {
        return ID_PREFIX;
    }

    @Override
    public String getTitle(RequestContext request) {
        return "Oracle Employee List";
    }

    @Override
    public String getAuthor(RequestContext request) throws ResponseContextException {
        return "Oracle Science";
    }


    @Override
    public String getId(Employee entry) throws ResponseContextException {
        return ID_PREFIX + entry.getId();
    }

    @Override
    public String getName(Employee entry) throws ResponseContextException {
        // TODO Auto-generated method stub
        return entry.getName();
    }

    @Override
    public Text getSummary(Employee entry, RequestContext request) throws ResponseContextException {
        return null;
    }

    @Override
    public String getTitle(Employee entry) throws ResponseContextException {
        // TODO Auto-generated method stub
        return entry.getName();
    }

    @Override
    public Date getUpdated(Employee entry) throws ResponseContextException {
        // TODO Auto-generated method stub
        return entry.getUpdated();
    }


    @Override
    public Object getContent(Employee employee, RequestContext request) throws ResponseContextException {
        Content content = factory.newContent(Content.Type.TEXT);
        Gson gson =  new GsonBuilder().setPrettyPrinting().create();
        content.setText("\n "+gson.toJson(employee).replace("}"," }\n"));
        return content;
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

    @Override
    public Employee postEntry(String title,
                              IRI id,
                              String summary,
                              Date updated,
                              List<Person> authors,
                              Content content,
                              RequestContext request) throws ResponseContextException {
        Gson gson = new Gson();
        Employee employee = gson.fromJson(content.getText(), Employee.class);
        employee.setUpdated(updated);
        employees.put(employee.getId(), employee);
        return employee;
    }

    @Override
    public void putEntry(Employee employee,
                         String title,
                         Date updated,
                         List<Person> authors,
                         String summary,
                         Content content,
                         RequestContext request) throws ResponseContextException {
        // TODO Auto-generated method stub
        employee.setName(title);
        employee.setUpdated(updated);

    }

    @Override
    public void deleteEntry(String resourceName, RequestContext request) throws ResponseContextException {
        Integer id = getIdFromResourceName(resourceName);
        employees.remove(id);
    }

    private Integer getIdFromResourceName(String resourceName) throws ResponseContextException {
//        int idx = resourceName.indexOf("-");
//        if (idx == -1) {
//            throw new ResponseContextException(404);
//        }
//        return new Integer(resourceName.substring(0, idx));
        return new Integer(resourceName);
    }

}
