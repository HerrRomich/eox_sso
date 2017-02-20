package com.ebase.eox.potral.shared;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@Component(service = Resource.class,
    property = {HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PREFIX + "=/content",
        HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PATTERN + "=/app/shared/*",
        HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=("
            + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=com.ebase.eox.appcontent)"},
    scope = ServiceScope.SINGLETON)
public class Resource {
}
