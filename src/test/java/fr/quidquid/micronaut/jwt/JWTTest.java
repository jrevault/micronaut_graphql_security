package fr.quidquid.micronaut.jwt;

import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JWTTest {

  @Test
  public void test_signature_base_64() {

    String token = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyMzUxMjM1MTM0NTQxMyIsImlzcyI6IkRETSIsImV4cCI6MTcwNjgzNzI5OCwiaWF0IjoxNjA2ODM2Mzk4LCJ1c2VyX2lkIjo4MDAsInJvbGVzIjpbIkFETUlOIl0sInVzZXJfZmlyc3RfbmFtZSI6IkFkIiwidXNlcl9sYXN0X25hbWUiOiJNaW4iLCJ1c2VyX2VtYWlsIjoiYWRtaW5Ad2hhdGV2ZXIuY29tIn0.L_TdO2SKAuOyXkw1yNtonyL3LMH8M14peQ42sx15JxQKEC-mTAl6IlSA8ZUW-FkT_-9V01eW_rb9PtVhgmRyhQ";
    String secret = "dGVzdA==";

    try {
      Jwt decodedToken = Jwts.parser( ).setSigningKey( secret ).parse( token );
      assertNotNull( decodedToken.getBody( ) );
    }
    catch ( ExpiredJwtException e ) {
      fail( "Failed authentication: token expired" );
    }
    catch ( SignatureException e ) {
      fail("Failed authentication: token signature verification failed" );
    }
    catch ( JwtException e ) {
      fail("Failed authentication: " + e.getMessage( ) );
    }
  }

  @Test
  public void test_signature() {

    String token = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyMzUxMjM1MTM0NTQxMyIsImlzcyI6IkRETSIsImV4cCI6MTcwNjgzNzI5OCwiaWF0IjoxNjA2ODM2Mzk4LCJ1c2VyX2lkIjo4MDAsInJvbGVzIjpbIkFETUlOIl0sInVzZXJfZmlyc3RfbmFtZSI6IkFkIiwidXNlcl9sYXN0X25hbWUiOiJNaW4iLCJ1c2VyX2VtYWlsIjoiYWRtaW5Ad2hhdGV2ZXIuY29tIn0.L_TdO2SKAuOyXkw1yNtonyL3LMH8M14peQ42sx15JxQKEC-mTAl6IlSA8ZUW-FkT_-9V01eW_rb9PtVhgmRyhQ";
    String secret = "test";

    // Let's check signature
    try {
      Jwt decodedToken = Jwts.parser( ).setSigningKey( secret.getBytes( StandardCharsets.UTF_8 ) ).parse( token );
      assertNotNull( decodedToken.getBody( ) );
    }
    catch ( ExpiredJwtException e ) {
      fail( "Failed authentication: token expired" );
    }
    catch ( SignatureException e ) {
      fail("Failed authentication: token signature verification failed" );
    }
    catch ( JwtException e ) {
      fail("Failed authentication: " + e.getMessage( ) );
    }
  }

}