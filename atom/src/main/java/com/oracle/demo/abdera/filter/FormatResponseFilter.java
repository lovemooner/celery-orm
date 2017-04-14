package com.oracle.demo.abdera.filter;


/**
 * User: lovemooner
 * Date: 17-4-13
 * Time: 下午2:35
 */

import org.apache.abdera.protocol.server.Filter;
import org.apache.abdera.protocol.server.FilterChain;
import org.apache.abdera.protocol.server.RequestContext;
import org.apache.abdera.protocol.server.ResponseContext;
import org.apache.abdera.protocol.server.context.ResponseContextWrapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FormatResponseFilter implements Filter {

    @Override
    public ResponseContext filter(RequestContext request, FilterChain chain) {
        String method = request.getMethod();
        if (!"GET".equals(method)) {
            return chain.next(request);
        }
        return new FormatResponseContext(chain.next(request));
    }

    private class FormatResponseContext extends ResponseContextWrapper {
        public FormatResponseContext(ResponseContext response) {
            super(response);
        }

        public void writeTo(OutputStream outputStream) throws IOException {
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            super.writeTo(ostream);
            byte[] data = ostream.toByteArray();
            String str=new String(data);
            data = str.replaceAll("><", ">\n<").getBytes();
            outputStream.write(data, 0, data.length);
            outputStream.close();

        }
    }
}
