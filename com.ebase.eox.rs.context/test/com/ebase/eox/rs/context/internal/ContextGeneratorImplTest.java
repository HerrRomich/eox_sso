package com.ebase.eox.rs.context.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.ebase.eox.rs.ContextGenerator;
import com.ebase.eox.rs.RestApplication;

public class ContextGeneratorImplTest {

  private ContextGenerator contextGeneratorUnderTest;
  @Mock
  private RestApplication application;

  @Before
  public void setUp() throws Exception {
    contextGeneratorUnderTest = new ContextGeneratorImpl();
  }

  @Test
  public void testGenerateContext() {
    String context = contextGeneratorUnderTest.generateContext(application);

    assertEquals(ContextGeneratorImpl.DEFAULT_CONTEXT, context);
  }

}
