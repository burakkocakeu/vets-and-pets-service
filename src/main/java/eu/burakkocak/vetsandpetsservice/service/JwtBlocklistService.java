package eu.burakkocak.vetsandpetsservice.service;

import java.util.Date;

public interface JwtBlocklistService {
    void blockToken(String token, Date expirationDate);
    boolean isTokenBlocked(String token);
}
