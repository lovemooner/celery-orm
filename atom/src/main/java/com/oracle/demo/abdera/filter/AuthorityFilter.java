package com.oracle.demo.abdera.filter;

/**
 * User: lovemooner
 * Date: 17-4-13
 * Time: 上午11:08
 */
import org.apache.abdera.protocol.server.Filter;
import org.apache.abdera.protocol.server.FilterChain;
import org.apache.abdera.protocol.server.ProviderHelper;
import org.apache.abdera.protocol.server.RequestContext;
import org.apache.abdera.protocol.server.ResponseContext;
import org.apache.abdera.protocol.server.Target;

public class AuthorityFilter implements Filter {
	private FilterChain chain;
	private RequestContext request;

	public ResponseContext filter(RequestContext request, FilterChain chain) {
		this.chain = chain;
		this.request = request;

		Target target = request.getTarget();
		String method = request.getMethod();
		String collection = target.getParameter("collection");

		if ("employee".equals(collection)) {
			return authorityControl(method, true, false, false, false);
		}

		if ("common".equals(collection)) {
			return authorityControl(method, false, true, true, true);
		}

		return ProviderHelper.forbidden(request);
	}

	private ResponseContext authorityControl(String method, boolean get,
			boolean put, boolean post, boolean delete) {
		ResponseContext context = chain.next(request);
		if ("GET".equals(method)) {
			if (!get)
				context = ProviderHelper.notallowed(request);
		}
		if ("PUT".equals(method)) {
			if (!put)
				context = ProviderHelper.notallowed(request);
		}
		if ("POST".equals(method)) {
			if (!post)
				context = ProviderHelper.notallowed(request);
		}
		if ("DELETE".equals(method)) {
			if (!delete)
				context = ProviderHelper.notallowed(request);
		}
		return context;
	}
}
