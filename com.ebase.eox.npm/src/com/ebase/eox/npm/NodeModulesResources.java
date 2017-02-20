package com.ebase.eox.npm;


import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@Component(service = NodeModulesResources.class, property = {
    HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PREFIX + "=/content",
    HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PATTERN + "=/*",
    HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=("
        + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=com.ebase.eox.npm)"},
    scope = ServiceScope.SINGLETON)
public class NodeModulesResources {

}
