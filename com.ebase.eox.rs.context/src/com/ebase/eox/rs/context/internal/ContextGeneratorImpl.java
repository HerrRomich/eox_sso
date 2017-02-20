package com.ebase.eox.rs.context.internal;

import org.osgi.service.component.annotations.Component;

import com.ebase.eox.rs.ContextGenerator;
import com.ebase.eox.rs.RestApplication;

@Component(immediate = true, service = ContextGenerator.class)
public class ContextGeneratorImpl implements ContextGenerator {

  static final String DEFAULT_CONTEXT = "/rest/*";

  @Override
  public String generateContext(RestApplication application) {
    return DEFAULT_CONTEXT;
  }

}
