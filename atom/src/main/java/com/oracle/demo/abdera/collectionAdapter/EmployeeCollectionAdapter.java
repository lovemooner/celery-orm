package com.oracle.demo.abdera.collectionAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oracle.demo.dao.EmployeesDao;
import com.oracle.demo.model.Employee;
import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.Person;
import org.apache.abdera.model.Text;
import org.apache.abdera.protocol.server.RequestContext;
import org.apache.abdera.protocol.server.context.ResponseContextException;
import org.apache.abdera.protocol.server.impl.AbstractEntityCollectionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.JavaCompiler;

public final class EmployeeCollectionAdapter extends AbstractEntityCollectionAdapter<Employee> {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeCollectionAdapter.class);

    public static final String ID_PREFIX = "IRI:";
    private Factory factory = new Abdera().getFactory();

    private EmployeesDao dao = new EmployeesDao();


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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        content.setText("\n " + gson.toJson(employee).replace("}", " }\n"));
        return content;
    }

    @Override
    public Iterable<Employee> getEntries(RequestContext request) throws ResponseContextException {
        try {
            return dao.findAll();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Employee getEntry(String resourceName, RequestContext request) throws ResponseContextException {
        Integer id = getIdFromResourceName(resourceName);
        try {
            return dao.findById(id);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
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
        try {
            if (dao.findById(employee.getId()) != null) {
                LOG.warn("employee is existed:", content.getText());
            } else {
                employee.setUpdated(new java.sql.Date(updated.getTime()));
                dao.addEmployees(employee);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
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
        employee.setName(title);
        employee.setUpdated(new java.sql.Date(updated.getTime()));
        try {
            dao.update(employee);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void deleteEntry(String resourceName, RequestContext request) throws ResponseContextException {
        Integer id = getIdFromResourceName(resourceName);
        try {
            Employee employee = dao.findById(id);
            dao.delete(employee);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
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
