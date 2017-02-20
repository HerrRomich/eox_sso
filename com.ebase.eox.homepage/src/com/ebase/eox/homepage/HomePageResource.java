package com.ebase.eox.homepage;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

@Component(immediate = true, service = HomePageResource.class,
    property = {HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PATTERN + "=/eox/*",
        HttpWhiteboardConstants.HTTP_WHITEBOARD_RESOURCE_PREFIX + "=/resources"})
public class HomePageResource {

}
