package com.oracle.demo.abdera.client;

import com.google.gson.Gson;
import com.oracle.demo.model.Employee;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.protocol.Response.ResponseType;
import org.apache.abdera.protocol.client.AbderaClient;
import org.apache.abdera.protocol.client.ClientResponse;
import org.apache.abdera.protocol.client.RequestOptions;
import org.apache.abdera.util.EntityTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static com.oracle.demo.abdera.collectionAdapter.EmployeeCollectionAdapter.ID_PREFIX;

public class Client {
//    private static final Logger LOG = LoggerFactory.getLogger(Client.class);
    private static AtomicInteger nextId = new AtomicInteger(1000);

    public static void testGET(String url) {
        Abdera abdera = Abdera.getInstance();
        AbderaClient client = new AbderaClient(abdera);
        ClientResponse resp = client.get(url);
        if (resp.getType() == ResponseType.SUCCESS) {
            Document<Feed> doc = resp.getDocument();
            System.out.println(doc.getRoot());
            EntityTag entityTag = resp.getEntityTag();
            System.out.println(entityTag.getTag());
        } else {
            // there was an error
            System.out.println("GET error:" + resp.getStatus() + " " + resp.getStatusText());
        }
    }

    public static void testPost(String url, int id) {
        Abdera abdera = Abdera.getInstance();
        AbderaClient client = new AbderaClient(abdera);
        Entry entry = abdera.newEntry();

        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("Tom");
        employee.setAge(30);

        Gson gson = new Gson();
        entry.setContent(gson.toJson(employee));
        entry.setId(ID_PREFIX + employee.getId());
        entry.setTitle(employee.getName());
        entry.setUpdated(new Date());
        RequestOptions opts = new RequestOptions();
        opts.setContentType("application/atom+xml;type=entry");
        ClientResponse resp = client.post(url, entry, opts);
        if (resp.getType() == ResponseType.SUCCESS) {
            Document<Feed> doc = resp.getDocument();
            System.out.println("Post success:" + doc.getRoot());
        } else {
            System.out.println("Post error:" + resp.getStatus() + " " + resp.getStatusText());
        }
    }

    public static void testPut(String url, int id) {
        Abdera abdera = Abdera.getInstance();
        AbderaClient client = new AbderaClient(abdera);
        Entry entry = abdera.newEntry();
        entry.setId(ID_PREFIX + id);
        entry.setUpdated(new Date());
        entry.setTitle("put_name");
         entry.addLink("");
        ClientResponse resp = client.put(url + "/" + id, entry);

        if (resp.getType() == ResponseType.SUCCESS) {
//            Document<Feed> doc = resp.getDocument();
//            System.out.println(doc.getRoot());
            System.out.println("PUT Success");
        } else {
            System.out.println("Put error:" + resp.getStatus() + " " + resp.getStatusText());
        }
    }

    public static void testDelete(String url, int id) {
        Abdera abdera = Abdera.getInstance();
        AbderaClient client = new AbderaClient(abdera);
        RequestOptions opts = new RequestOptions();
        opts.setContentType("application/atom+xml");
        ClientResponse resp = client.delete(url + "/" + id, opts);
        if (resp.getType() == ResponseType.SUCCESS) {
            System.out.println("Delete Success");
        } else {
            System.out.println("Delete Error:" + resp.getStatus() + " " + resp.getStatusText());
        }
    }

    public static void main(String[] args) throws ParseException {
        String url = "http://localhost:8080/web/employee";
//        String url = "http://slc11fsp.us.oracle.com:8080/atom/employee";
        int id = 1001;
//        testGET(url);
        testPost(url, id);
//        testPut(url,id);
//        testDelete(url, id);


    }
}
