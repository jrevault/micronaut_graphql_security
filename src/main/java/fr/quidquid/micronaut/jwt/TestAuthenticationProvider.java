package fr.quidquid.micronaut.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.jsonwebtoken.*;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.*;
import io.reactivex.Maybe;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class TestAuthenticationProvider implements AuthenticationProvider {
  private static final Logger logger = LoggerFactory.getLogger( TestAuthenticationProvider.class );

  @Property(name = "micronaut.security.token.jwt.signatures.secret.generator.secret")
  String secret = "test";

  @Override
  public Publisher<AuthenticationResponse> authenticate( @Nullable HttpRequest<?> httpRequest , AuthenticationRequest<?, ?> authenticationRequest ) {
    logger.warn( "authenticate !" );

    return Maybe.<AuthenticationResponse>create( emitter -> {
      Optional<String> tokenHeader = httpRequest.getHeaders( ).findFirst( "Authorization" );
      if ( tokenHeader.isPresent( ) ) {
        try {
          String[] auth_bearer = tokenHeader.get().split(" ");
          String token = auth_bearer[1];
          logger.warn( "JWT token from headers {}", token );

            // Check signature
          Jwt decodedToken = Jwts.parser( ).setSigningKey( secret ).parse( token );
          logger.warn( "Successful validated decoded token :\n{}" , decodedToken.toString( ) );

          DecodedJWT decoded_token = JWT.decode( token );

          Map<String, Object> attributes = new HashMap<>( );
          attributes.put( "user_id" , decoded_token.getClaim( "user_id" ).asString( ) );
          attributes.put( "user_first_name" , decoded_token.getClaim( "user_id" ).asString( ) );
          attributes.put( "user_last_name" , decoded_token.getClaim( "user_id" ).asString( ) );

          UserDetails userDetails = new UserDetails(
              decoded_token.getClaim( "user_email" ).asString( ),
              decoded_token.getClaim( "roles" ).asList( String.class ) ,
              attributes
          );

          emitter.onSuccess( userDetails );
        }
        catch ( ExpiredJwtException e ) {
          logger.error( "Failed authentication with token {} which has already expired" , tokenHeader.get( ) , e );
          throw new AuthenticationException( new AuthenticationFailed( "Failed authentication: token expired" ) );
        }
        catch ( SignatureException e ) {
          logger.error( "Failed authentication with token {} which signature's verification failed" ,
              tokenHeader.get( ) , e
          );
          throw new AuthenticationException( new AuthenticationFailed( "Failed authentication: token signature verification failed" ) );
        }
        catch ( JwtException e ) {
          logger.error( "Failed authentication with token {}" , tokenHeader.get( ) , e );
          throw new AuthenticationException( new AuthenticationFailed( "Failed authentication: " + e.getMessage( ) ) );
        }
      }
    } ).toFlowable( );
  }


}