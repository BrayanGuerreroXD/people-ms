package co.com.pragma.peoplems.usecase.login;

import co.com.pragma.peoplems.model.auth.Auth;

public interface LoginService {
    Auth login(Auth auth);
}
