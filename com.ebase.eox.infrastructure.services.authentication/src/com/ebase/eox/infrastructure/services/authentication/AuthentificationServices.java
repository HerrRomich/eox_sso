package com.ebase.eox.infrastructure.services.authentication;

import com.ebase.eox.infrastructure.services.RestService;

public interface AuthentificationServices extends RestService {

  String login(Authentication authentication) throws AuthenticationException;
  
  void logut();
  
}
