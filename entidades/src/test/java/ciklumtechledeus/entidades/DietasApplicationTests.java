package ciklumtechledeus.entidades;

import java.net.URI;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import ciklumtechledeus.entidades.controllers.DietaRest;
import ciklumtechledeus.entidades.dtos.DietaDTO;
import ciklumtechledeus.entidades.dtos.DietaNuevaDTO;
import ciklumtechledeus.entidades.entities.Dieta;
import ciklumtechledeus.entidades.exceptions.AccesoProhibido;
import ciklumtechledeus.entidades.repositories.DietaRepository;
import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

//---------------------------------------------------------
import ciklumtechledeus.entidades.seguridad.JwtRequestFilter;
import ciklumtechledeus.entidades.seguridad.JwtUtil;
import ciklumtechledeus.entidades.seguridad.SecurityConfguration;
import ciklumtechledeus.entidades.services.DietaServicio;

import org.junit.platform.engine.reporting.ReportEntry;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.http.*;
//-------------------------------------------------------

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.datasource.url=jdbc:h2:mem:db")
@DisplayName("En el servicio de gestion de dietas")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DietasApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value = "${local.server.port}")
	private int port;

	private DietaRest dietarest;

	@Autowired
	private DietaRepository repoDieta;

	//--------------------------------------
	@Autowired
    private JwtUtil jwtTokenUtil;

	private String token;

	//---------------------------------------

	@BeforeEach
	public void initializeDatabase() {
		//token = jwtTokenUtil.generateToken(User user = new User());
		repoDieta.deleteAll();
	}

	private URI uri(String scheme, String host, int port, String... paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder()
            .scheme(scheme)
            .host(host).port(port);
        for (String path : paths) {
            ub = ub.path(path);
        }
        return ub.build();
	}

	private URI uriQuery(String scheme, String host, int port, String path, Map<String, Collection<String>> query){
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port)
				.path(path);
		for(Map.Entry<String, Collection<String>> entry : query.entrySet()) ub.queryParam(entry.getKey(), entry.getValue());

		return ub.build();
	}

	//Para descubrir las capacidades de comunicación del servidor y los métodos HTTP permitidos para un recurso.
	private RequestEntity<Void> options(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.options(uri)
			.build();
		return peticion;
	}

	//Para obtener los metadatos de un recurso sin obtener el cuerpo del recurso.
	private RequestEntity<Void> head(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.head(uri)
			.build();
		return peticion;
	}
		//--------------------------------------------------------------------------------------
	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.get(uri)
			.headers(getHeaders())
			.accept(MediaType.APPLICATION_JSON)
			.build();
		return peticion;
	}

	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.delete(uri)
		.headers(getHeaders())
			.build();
		return peticion;
	}
	
	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.post(uri)
		.headers(getHeaders())
			.contentType(MediaType.APPLICATION_JSON)
			.body(object);
		return peticion;
	}
	
	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host, port, path);
		var peticion = RequestEntity.put(uri)
		.headers(getHeaders())
			.contentType(MediaType.APPLICATION_JSON)
			.body(object);
		return peticion;
	}
	//-----------------------------------------------------------------------------------------

	private void compruebaCampos(Dieta expected, Dieta actual){
		assertThat(actual.getId()).isEqualTo(expected.getId());
//		assertThat(actual.getClienteId()).isEqualTo(expected.getClienteId());
//		assertThat(actual.getEntrenadorId()).isEqualTo(expected.getClienteId());
	}

	//-----------------------------------------------------------------------
	private HttpHeaders getHeaders() {
		UserDetails userDetails = new User("admin", "admin", Collections.emptyList());
		
		String token = jwtTokenUtil.generateToken(userDetails);

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
	//------------------------------------------------------------------------


	@Nested
	@DisplayName("cuando no hay dietas")
	@Transactional
	public class BaseDeDatosSinDatos{

		//----GET /dieta --------------------//
		@Test
		@DisplayName("Error en solicitud por introducir ambos parámetros para obtener dieta en Base de datos vacia")
		public void errorPorAmbosParametros() {
			//String token = obtenerTokenConRol("ENTRENADOR");
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			UserDetails userDetails = new User("jaime", "1234", authorities); 
    		String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			ResponseEntity<List<DietaDTO>> respuesta = restTemplate.exchange(
					"/dieta?entrenador=1&cliente=1", HttpMethod.GET, entity, 
					new ParameterizedTypeReference<List<DietaDTO>>() {}
			);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		}

		@Test
		@DisplayName("devuelve NOT FOUND cuando no hay dietas de un entrenador en una base de datos vacia")
		public void devuelveListaDeDietasDeEntrenador() {
			// Simula la existencia de un entrenador
			Long entrenadorId = 1L;
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			UserDetails userDetails = new User("admin", "admin", authorities);	
			String token = jwtTokenUtil.generateToken(userDetails);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<Void> entity = new HttpEntity<>(headers);
			String url = "http://localhost:" + port + "/dieta?entrenador=" + entrenadorId;
			ResponseEntity<List<DietaDTO>> respuesta = restTemplate.exchange(
				url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<DietaDTO>>() {}
			);
			// Verifica que se recibe NOT FOUND y la lista está vacía
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			
		}

		@Test
		@DisplayName("devuelve NOT FOUND cuando no hay dieta asociada a un cliente")
		public void devuelveDietaDeCliente() {
			Long clienteId = 1L;
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE"));
			UserDetails userDetails = new User("adam", "admin", authorities);	
			String token = jwtTokenUtil.generateToken(userDetails);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);

			HttpEntity<Void> entity = new HttpEntity<>(headers);
			String url = "http://localhost:" + port + "/dieta?cliente=" + clienteId;

			ResponseEntity<List<DietaDTO>> respuesta = restTemplate.exchange(
					url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<DietaDTO>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);

		}
		// FIN de GET/dieta ------------//
		
		@Test
		@DisplayName("error al obtener una dieta concreta porque no hay datos")
		public void errorAlObtenerDietaConcreta(){
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE"));
			UserDetails userDetails = new User("adam", "admin", authorities);	
			String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);

			HttpEntity<Void> entity = new HttpEntity<>(headers);
			String url = "http://localhost:" + port + "/dieta/1";

			var respuesta = restTemplate.exchange(url, HttpMethod.GET,entity,
				new ParameterizedTypeReference<DietaDTO>() {});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);	
		}

		//------------ PUT/ dieta-----------------------------
		

		
		//----------------------- PUT/ dieta-----------------------------------

		//------------------DELETE /DIETA/ID DIETA
		@Test
		@DisplayName("devuelve error al eliminar una dieta que no existe")
		public void eliminarDietaInexistente() {
			//var peticion = delete("http", "localhost", port, "/dieta/999"); // ID no existente
			
			HttpHeaders headers = getHeaders();
			HttpEntity<Void> entity = new HttpEntity<>(headers);
			
			// Construye la URI para la solicitud
    		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/dieta/999")
            .build().toUri();


			try {
				restTemplate.exchange(uri, HttpMethod.DELETE, entity, Void.class);
			} catch (HttpClientErrorException e) {
				assertThat(e.getStatusCode().value()).isEqualTo(404); // Esperamos un 404
			}
		}


		@Test
		@DisplayName("inserta correctamente una dieta")
		public void insertaDieta() {
			var dieta = DietaNuevaDTO.builder()
								.nombre("Nueva")
								.descripcion("Nueva")
								.observaciones("Nueva")
								.objetivo("Nueva")
								.duracionDias(30)
								.recomendaciones("Nueva")
								.build();
			
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			UserDetails userDetails = new User("jaime", "1234", authorities); 
    		String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = getHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<DietaNuevaDTO> entity = new HttpEntity<>(dieta, headers);
			
			// Construye la URI para la solicitud
			var peticion = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/dieta")
                                       .queryParam("entrenador", 1)
                                       .build().toUri();
									   
			var respuesta = restTemplate.exchange(peticion, HttpMethod.POST, entity, DietaDTO.class);

		
			assertThat(respuesta.getStatusCode().value()).isEqualTo(201); // Esperamos un 201
		}
		//URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/dieta?entrenador=1")
        	//.build().toUri();
			//ResponseEntity<DietaDTO> respuesta = restTemplate.exchange(uri, HttpMethod.POST, entity, DietaDTO.class);

		

		

		//--------------------------------------------------

		
	}


	@Nested 
    @DisplayName("cuando la base de datos contiene datos")
    public class BaseDeDatosConDatos {
	
		@BeforeEach
		public void insertarDatos(){
			var dieta1 = new Dieta();
			Set<Long> set = new HashSet<>();
			set.add(1L);
			dieta1.setId(1L);
			dieta1.setNombre("Dieta 1");
			dieta1.setDescripcion("Dieta para definición");
			dieta1.setObservaciones("15000 pasos diarios");
			dieta1.setObjetivo("1800 kcal al dia");
			dieta1.setDuracionDias(30);
			dieta1.setEntrenadorId(1L);
			dieta1.setClienteId(set);
			repoDieta.save(dieta1);

			var dieta2 = new Dieta();
			dieta2.setId(2L);
			dieta2.setNombre("Dieta 2");
			dieta2.setDescripcion("Dieta para ganar masa muscular");
			dieta2.setObservaciones("uso de batidos de proteina");
			dieta2.setObjetivo("3500 kcal al dia");
			dieta2.setDuracionDias(30);
			dieta2.setEntrenadorId(1L);
			repoDieta.save(dieta2);
		}
		
		//----------------- GET /dieta --------------------------------
		

		@Test
		@DisplayName("devuelve la lista de dietas de un entrenador en una base de datos con datos")
		public void devuelveListaDeDietasDeEntrenador() {
			// Simula la existencia de un entrenador
			Long entrenadorId = 1L;
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			UserDetails userDetails = new User("jaime", "admin", authorities);	
			String token = jwtTokenUtil.generateToken(userDetails);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<Void> entity = new HttpEntity<>(headers);
			String url = "http://localhost:" + port + "/dieta?entrenador=" + entrenadorId;
			ResponseEntity<List<DietaDTO>> respuesta = restTemplate.exchange(
				url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<DietaDTO>>() {}
			);
			// Verifica que se recibe una respuesta exitosa y la lista está vacía
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isNotEmpty();
		}

		@Test
		@DisplayName("devuelve la lista de dietas de un cliente en una base de datos con datos")
		public void devuelveListaDeDietasDeCliente() {
			// Simula la existencia de un entrenador
			Long clienteId = 1L;
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE"));
			UserDetails userDetails = new User("jaime", "admin", authorities);	
			String token = jwtTokenUtil.generateToken(userDetails);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<Void> entity = new HttpEntity<>(headers);
			String url = "http://localhost:" + port + "/dieta?cliente=" + clienteId;
			ResponseEntity<List<DietaDTO>> respuesta = restTemplate.exchange(
				url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<DietaDTO>>() {}
			);
			// Verifica que se recibe una respuesta exitosa y la lista está vacía
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isNotEmpty();
		}

		@Test
		@DisplayName("Error en solicitud por introducir ambos parámetros para obtener dieta en Base de datos con datos")
		public void errorPorAmbosParametros() {
			//String token = obtenerTokenConRol("ENTRENADOR");
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			UserDetails userDetails = new User("jaime", "1234", authorities); 
    		String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			ResponseEntity<List<DietaDTO>> respuesta = restTemplate.exchange(
					"/dieta?entrenador=1&cliente=1", HttpMethod.GET, entity, 
					new ParameterizedTypeReference<List<DietaDTO>>() {}
			);

			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		}

		 
		@Test
		@DisplayName("Acceso no autorizado para entrenador incorrecto")
		public void accesoNoAutorizadoParaEntrenadorIncorrecto() {
			// Simula un usuario sin el rol de entrenador 
			// y no le añadimos seguridad  para que nos devuelva 403
			//List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ENTRENADOR"));
			//UserDetails userDetails = new User("otroEntrenador", "1234",Collections.emptyList());
			//String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			// Supongamos que el ID del entrenador real es 1, pero estamos usando un usuario diferente
			ResponseEntity<List<DietaDTO>> respuesta = restTemplate.exchange(
					"/dieta?entrenador=1", HttpMethod.GET, entity, 
					new ParameterizedTypeReference<List<DietaDTO>>() {}
			);

			// Verifica que la respuesta sea 403 Forbidden
			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("Acceso no autorizado para cliente incorrecto")
		public void accesoNoAutorizadoParaClienteIncorrecto() {
			// Simula un usuario sin el rol de cliente, 
			// y no le añadimos seguridad ni el rol para que nos devuelva 403
			//List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("CLIENTE"));
			//UserDetails userDetails = new User("otroCliente", "1234", authorities);
			//String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			// Supongamos que el ID del cliente real es 1, pero estamos usando un usuario diferente
			ResponseEntity<List<DietaDTO>> respuesta = restTemplate.exchange(
					"/dieta?cliente=1", HttpMethod.GET, entity, 
					new ParameterizedTypeReference<List<DietaDTO>>() {}
			);

			// Verifica que la respuesta sea 403 Forbidden
			assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}

		@Test
		@DisplayName("devuelve NOT FOUND cuando no hay dietas de un entrenador en una base de datos vacia")
		public void devuelveListaDeDietasDeEntrenadorNoEncontrada() {
			// Simula la existencia de un entrenador
			Long entrenadorId = 3L;
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			UserDetails userDetails = new User("admin", "admin", authorities);	
			String token = jwtTokenUtil.generateToken(userDetails);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<Void> entity = new HttpEntity<>(headers);
			String url = "http://localhost:" + port + "/dieta?entrenador=" + entrenadorId;
			ResponseEntity<List<DietaDTO>> respuesta = restTemplate.exchange(
				url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<DietaDTO>>() {}
			);
			// Verifica que se recibe NOT FOUND y la lista está vacía
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			
		}

		@Test
		@DisplayName("devuelve NOT FOUND cuando no hay dieta asociada a un cliente")
		public void devuelveDietaDeClienteNoEncontrada() {
			Long clienteId = 3L;
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE"));
			UserDetails userDetails = new User("adam", "admin", authorities);	
			String token = jwtTokenUtil.generateToken(userDetails);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);

			HttpEntity<Void> entity = new HttpEntity<>(headers);
			String url = "http://localhost:" + port + "/dieta?cliente=" + clienteId;

			ResponseEntity<List<DietaDTO>> respuesta = restTemplate.exchange(
					url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<DietaDTO>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);

		}
		//--------------- FIN GET/ dieta ----------------------------------


		//-------------- PUT /dieta -----------------------------------------
		
		@Test
        @DisplayName("actualiza una dieta correctamente")
        public void putDietaCorrectamente() {
            Long clienteId = 1L;
			var dieta = DietaDTO.builder()
								.id(1L)
                                .nombre("Actualizado")
                                .descripcion("Actualizado")
                                .observaciones("Actualizado")
                                .objetivo("Actualizado")
                                .duracionDias(30)
                                .recomendaciones("Actualizado")
                                .build();
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			UserDetails userDetails = new User("jaime", "1234", authorities); 
			String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = getHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<DietaDTO> entity = new HttpEntity<>(dieta, headers);
			
			String url = "http://localhost:" + port + "/dieta?cliente="+clienteId ;
			// Construye la URI para la solicitud
			var respuesta = restTemplate.exchange(url, HttpMethod.PUT, entity, DietaDTO.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(repoDieta.findById(1L).get().getNombre()).isEqualTo("Actualizado");
        }

		@Test
		@DisplayName("devuelve BAD REQUEST al intentar asociar dieta vacia a cliente ")
		public void asociaDietaVaciaCliente(){
			Long clienteId=5L;
			var dieta = DietaDTO.builder()
								.build();
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			UserDetails userDetails = new User("jaime", "1234", authorities); 
			String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = getHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<DietaDTO> entity = new HttpEntity<>(dieta, headers);
			
			String url = "http://localhost:" + port + "/dieta?cliente="+clienteId ;
			// Construye la URI para la solicitud
			var respuesta = restTemplate.exchange(url, HttpMethod.PUT, entity, DietaDTO.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(400);

		}

		@Test
		@DisplayName("devuelve error 404 cuando no hay dieta para actualizar ")
		public void actualizarDietaNoExistente(){
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			UserDetails userDetails = new User("entrenador", "password", authorities);
			String token = jwtTokenUtil.generateToken(userDetails);
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			
			Long clienteId = 1L;
			var dieta = DietaDTO.builder()
								.id(1L)
                                .nombre("Actualizado")
                                .descripcion("Actualizado")
                                .observaciones("Actualizado")
                                .objetivo("Actualizado")
                                .duracionDias(30)
                                .recomendaciones("Actualizado")
                                .build();
			HttpEntity<?> entity = new HttpEntity<>(dieta, headers);

			// Construye la URI para la solicitud PUT con un ID inexistente
			URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/dieta/9999")
											.build().toUri();
			
			// Realiza la solicitud PUT
			ResponseEntity<Void> respuesta = restTemplate.exchange(uri, HttpMethod.PUT, entity, Void.class);
			
			// Verifica que se devuelva el código de estado 404
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);

		}

		@Test
		@DisplayName("devuelve error de accesso no autorizado para actualizar dieta existente")
		public void putAccesoNoAutorizado(){
			Long clienteId = 1L;
			var dieta = DietaDTO.builder()
								.id(1L)
                                .nombre("Actualizado")
                                .descripcion("Actualizado")
                                .observaciones("Actualizado")
                                .objetivo("Actualizado")
                                .duracionDias(30)
                                .recomendaciones("Actualizado")
                                .build();
			//No le añadimos los roles ni el token para que no autorice el acceso
			//UserDetails userDetails = new User("jaime", "1234", authorities); 
			//String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = getHeaders();
			//headers.set("Authorization", "Bearer " + token);
			HttpEntity<DietaDTO> entity = new HttpEntity<>(dieta, headers);
			
			String url = "http://localhost:" + port + "/dieta?cliente="+clienteId ;
			// Construye la URI para la solicitud
			var respuesta = restTemplate.exchange(url, HttpMethod.PUT, entity, DietaDTO.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		}



		// ------------------- FIN put/dieta-------------------------
		
		// ------------------- DELETE/dieta/idDieta -------------------

		@Test
		@DisplayName("elimina dieta correctamente")
		public void eliminaDietaCorrectamente() {
			//var peticion = delete("http", "localhost", port, "/dieta/1");
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			UserDetails userDetails = new User("jaime", "1234", authorities); 
			String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = getHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			// Construye la URI para la solicitud
    		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/dieta/1")
            .build().toUri();

			ResponseEntity<Void> respuesta = restTemplate.exchange(uri, HttpMethod.DELETE, entity, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200); // Esperamos un 200
    	}

		@Test
		@DisplayName("devuelve error al intentar eliminar dieta que no existe")
		public void eliminaDietaInexistente() {
			//var peticion = delete("http", "localhost", port, "/dieta/999"); // ID no existente
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			UserDetails userDetails = new User("jaime", "1234", authorities); 
			String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = getHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			// Construye la URI para la solicitud
    		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/dieta/999")
            .build().toUri();

				var respuesta = restTemplate.exchange(uri, HttpMethod.DELETE, entity, Void.class);
				assertThat(respuesta.getStatusCode().value()).isEqualTo(404); // Esperamos un 404
		}

		@Test
		@DisplayName("devuelve error acceso no autorizado al intentar eliminar dieta sin rol de entrenador")
		public void eliminaDietaSinAccesoNoAutorizado() {
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE"));
			UserDetails userDetails = new User("jaime", "1234", authorities); 
			String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = getHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/dieta/1")
									.build().toUri();

			ResponseEntity<Void> respuesta = restTemplate.exchange(uri, HttpMethod.DELETE, entity, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		}
		//------------------- FIN DELETE/dieta/idDieta-----------

		@Test
		@DisplayName("da error cuando inserta dieta sin rol de entrenador")
		public void insertaDietaSinAccesoAutorizado(){
			var dieta = DietaDTO.builder()
						.nombre("Dieta 1")
						.descripcion("Dieta para definición")
						.observaciones("15000 pasos diarios")
						.objetivo("1800 kcal al dia")
						.duracionDias(30).build();
			var peticion = post("http", "localhost", port, "/dieta/1", dieta);	
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		}

		@Test
		@DisplayName("devuelve una dieta recibiendo un id con el entrenador asociado")
		public void getDietaByIdyEntrenadorAsociado(){
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			UserDetails userDetails = new User("adam", "admin", authorities);	
			String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);

			HttpEntity<Void> entity = new HttpEntity<>(headers);
			String url = "http://localhost:" + port + "/dieta/1?entrenador=1";

			var respuesta = restTemplate.exchange(url, HttpMethod.GET,entity,
				new ParameterizedTypeReference<DietaDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getId()).isEqualTo(1L);
		}

		@Test
		@DisplayName("devuelve una dieta recibiendo un id con el cliente asociado")
		public void getDietaByIdyClienteAsociado(){
			List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE"));
			UserDetails userDetails = new User("adam", "admin", authorities);	
			String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);

			HttpEntity<Void> entity = new HttpEntity<>(headers);
			String url = "http://localhost:" + port + "/dieta/1?cliente=1";

			var respuesta = restTemplate.exchange(url, HttpMethod.GET,entity,
				new ParameterizedTypeReference<DietaDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getId()).isEqualTo(1L);
		}

		@Test
		@DisplayName("devuelve ERROR al obtener una dieta recibiendo un id con el entrenador NO asociado")
		public void getDietaByIdyEntrenadorNoAsociado(){
			//List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ENTRENADOR"));
			//UserDetails userDetails = new User("adam", "admin", authorities);	
			//String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = new HttpHeaders();
			//headers.set("Authorization", "Bearer " + token);

			HttpEntity<Void> entity = new HttpEntity<>(headers);
			String url = "http://localhost:" + port + "/dieta/2?entrenador=5";

			var respuesta = restTemplate.exchange(url, HttpMethod.GET,entity,
				new ParameterizedTypeReference<DietaDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		
		}

		@Test
		@DisplayName("devuelve ERROR al obtener una dieta recibiendo un id con el cliente NO asociado")
		public void getDietaByIdyClienteNoAsociado(){
			//List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENTE"));
			//UserDetails userDetails = new User("adam", "admin", authorities);	
			//String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = new HttpHeaders();
			//headers.set("Authorization", "Bearer " + token);

			HttpEntity<Void> entity = new HttpEntity<>(headers);
			String url = "http://localhost:" + port + "/dieta/2?cliente=7";

			var respuesta = restTemplate.exchange(url, HttpMethod.GET,entity,
				new ParameterizedTypeReference<DietaDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
		}


	}

}
