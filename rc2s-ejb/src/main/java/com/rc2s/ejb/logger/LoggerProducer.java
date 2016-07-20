package com.rc2s.ejb.logger;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Singleton
public class LoggerProducer
{
    @Produces
    public Logger createLogger(InjectionPoint injectionPoint)
    {
        return LogManager.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
