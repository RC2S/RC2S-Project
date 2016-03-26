package com.rc2s.ejb.streaming;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

@Stateless(mappedName = "StreamingEJB")
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class StreamingFacadeBean implements StreamingFacadeRemote
{
	
}
