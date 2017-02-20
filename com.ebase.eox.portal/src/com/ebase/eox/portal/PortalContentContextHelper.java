package com.ebase.eox.portal;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@Component(service = ServletContextHelper.class,
property = {
    HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME
        + "=com.ebase.eox.appcontent",
    HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH + "=/portal"}, scope=ServiceScope.BUNDLE)
public class PortalContentContextHelper extends DelegatedServletContextHalper {

}
