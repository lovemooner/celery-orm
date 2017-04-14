package com.oracle.demo.abdera.server;

import com.oracle.demo.abdera.filter.AuthorityFilter;
import com.oracle.demo.abdera.filter.FormatResponseFilter;
import org.apache.abdera.protocol.server.Provider;
import org.apache.abdera.protocol.server.impl.DefaultProvider;
import org.apache.abdera.protocol.server.impl.SimpleWorkspaceInfo;
import org.apache.abdera.protocol.server.servlet.AbderaServlet;

import com.oracle.demo.abdera.collectionAdapter.EmployeeCollectionAdapter;
import com.oracle.demo.abdera.collectionAdapter.SimpleAdapter;

public class EmployeeProviderServlet extends AbderaServlet {
	protected Provider createProvider() {
		EmployeeCollectionAdapter ca = new EmployeeCollectionAdapter();
		ca.setHref("employee");
		SimpleAdapter sa = new SimpleAdapter();
		sa.setHref("employee2");

		SimpleWorkspaceInfo wi = new SimpleWorkspaceInfo();
		wi.setTitle("Employee Directory Workspace");
		wi.addCollection(ca);
		wi.addCollection(sa);

		DefaultProvider provider = new DefaultProvider("/");
		provider.addWorkspace(wi);

        AuthorityFilter authorityFilter = new AuthorityFilter();
        FormatResponseFilter responseFilter=new FormatResponseFilter();
//        provider.addFilter(authorityFilter);
        provider.addFilter(responseFilter);
        provider.init(getAbdera(), null);

		return provider;
	}

}