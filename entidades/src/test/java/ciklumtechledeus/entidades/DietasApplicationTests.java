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

		/*
		@Test
			@DisplayName("devuelve una lista de dietas introduciendo id de cliente en una base de datos vacia")
			public void devuelveListaDietasCliente() {
				var peticion = RequestEntity.get("http://localhost:"+ port+ "/rutina?cliente=1")
						.headers(getHeaders())
						.build();

				var respuesta = restTemplate.exchange(peticion,
						new ParameterizedTypeReference<List<DietaDTO>>() {});

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				assertThat(respuesta.getBody().size()).isEqualTo(0);
			}
		
		
			@Test
			@DisplayName("devuelve una lista de dietas introduciendo id de entrenador en una base de datos vacia")
			public void devuelveListaDietasEntrenador() {
				var peticion = RequestEntity.get("http://localhost:"+ port+ "/rutina?entrenador=1")
						.headers(getHeaders())
						.build();

				var respuesta = restTemplate.exchange(peticion,
						new ParameterizedTypeReference<List<DietaDTO>>() {});

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				assertThat(respuesta.getBody().size()).isEqualTo(0);
			}*/

		@SuppressWarnings("null")
		@Test
		@DisplayName("devuelve la lista de dietas")
		public void devuelveDietas() {
			var peticion = get("http", "localhost", port, "/dieta/1");

			var respuesta = restTemplate.exchange(peticion,
				new ParameterizedTypeReference<List<DietaDTO>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		
		@Test
		@DisplayName("error al obtener una dieta concreto")
		public void errorAlObtenerDietaConcreta(){
			var peticion = get("http", "localhost", port, "/dieta/1");

			var respuesta = restTemplate.exchange(peticion, 
				new ParameterizedTypeReference<DietaDTO>() {});
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);	
		}

		@Test
		@DisplayName("devuelve error al modificar una dieta que no existe")
		public void modificarDietaInexistente(){
			var dieta = DietaDTO.builder().id(1L)
										.nombre("Jaime")
										.descripcion("baja en caloria")
										.observaciones("100 g proteinas necesarias")
										.objetivo("2 kilos en un mes")
										.duracionDias(30)
										.recomendaciones("beber mucha agua")
										.build();
			var peticion = put("http", "localhost", port, "/dieta/1", dieta);
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);			
		}

		/* 
		@Test
		@DisplayName("devuelve error al eliminar una dieta que no existe")
		public void eliminarDietaInexistente(){
			var peticion = delete("http", "localhost", port, "/dieta/1");
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		*/
		/* 
		@Test
		@DisplayName("inserta correctamente una dieta")
		public void insertaDieta(){
			var dieta = DietaDTO.builder()
						.id(1L)
						.nombre("Jaime")
						.descripcion("baja en caloria")
						.observaciones("100 g proteinas necesarias")
						.objetivo("2 kilos en un mes")
						.duracionDias(30)
						.recomendaciones("beber mucha agua")
						.build();
			var peticion = post("http", "localhost", port, "/dieta/1", dieta);
			var respuesta = restTemplate.exchange(peticion, Void.class);

			//Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.startsWith("http://localhost:"+port+"/dieta");
			
			List<Dieta> dietas = repoDieta.findAll();
			assertThat(dietas).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.endsWith("/"+dietas.get(0).getId());
		}
		*/
		/*    @Test
		@DisplayName("devuelve error cuando no aporto información del ingrediente al insertar producto")
		public void errorAlInsertarProductoSinInfoDeIngrediente() {

			var ingrediente = IngredienteDTO.builder().build();
			
			// Preparamos el producto a insertar
			var producto = ProductoDTO.builder()
					.nombre("Hamburguesa")
					.ingredientes(Collections.singleton(ingrediente))
					.build();
			// Preparamos la petición con el ingrediente dentro
			var peticion = post("http", "localhost",port, "/productos", producto);

			// Invocamos al servicio REST 
			var respuesta = restTemplate.exchange(peticion,Void.class);

			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}*/

		//--------------------------------------------------
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
			

			UserDetails userDetails = new User("jaime", "1234", Collections.emptyList()); 
    		String token = jwtTokenUtil.generateToken(userDetails);
			HttpHeaders headers = getHeaders();
			headers.set("Authorization", "Bearer " + token);
			HttpEntity<?> entity = new HttpEntity<>(dieta, headers);
			
			// Construye la URI para la solicitud
    		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/dieta?entrenador=1")
        .build().toUri();
			ResponseEntity<DietaDTO> respuesta = restTemplate.exchange(uri, HttpMethod.POST, entity, DietaDTO.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201); // Esperamos un 201
		}
	
		

		@Test
		@DisplayName("devuelve la lista de dietas de un entrenador")
		public void devuelveListaDeDietasDeEntrenador() {
			// Simula la existencia de un entrenador
			Long entrenadorId = 1L;
			UserDetails userDetails = new User("admin", "admin", Collections.emptyList());	
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
			assertThat(respuesta.getBody()).isEmpty();
		}

		@Test
		@DisplayName("devuelve la dieta asociada a un cliente")
		public void devuelveDietaDeCliente() {
			Long clienteId = 1L;
			UserDetails userDetails = new User("admin", "admin", Collections.emptyList());	
			String token = jwtTokenUtil.generateToken(userDetails);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);

			HttpEntity<Void> entity = new HttpEntity<>(headers);
			String url = "http://localhost:" + port + "/dieta?cliente=" + clienteId;

			ResponseEntity<List<DietaDTO>> respuesta = restTemplate.exchange(
					url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<DietaDTO>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
		}

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
			dieta1.setClienteId(set);
			repoDieta.save(dieta1);

			var dieta2 = new Dieta();
			dieta2.setId(2L);
			dieta2.setNombre("Dieta 2");
			dieta2.setDescripcion("Dieta para ganar masa muscular");
			dieta2.setObservaciones("uso de batidos de proteina");
			dieta2.setObjetivo("3500 kcal al dia");
			dieta2.setDuracionDias(30);
			repoDieta.save(dieta2);
		}

		//Revisar
		@Test
		@DisplayName("da error cuando inserta dieta existente")
		public void insertaDietaExistente(){
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
		@DisplayName("devuelve una dieta recibiendo un id")
		public void getDietaById(){
			var peticion = get("http", "localhost", port, "/dieta/2");
			var respuesta = restTemplate.exchange(peticion, DietaDTO.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getId()).isEqualTo(2L);
		}
		
		@Test
		@DisplayName("elimina dieta correctamente")
		public void eliminaDietaCorrectamente() {
			//var peticion = delete("http", "localhost", port, "/dieta/1");
			HttpHeaders headers = getHeaders();
			HttpEntity<Void> entity = new HttpEntity<>(headers);

			// Construye la URI para la solicitud
    		URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + "/dieta/1")
            .build().toUri();

			ResponseEntity<Void> respuesta = restTemplate.exchange(uri, HttpMethod.DELETE, entity, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200); // Esperamos un 200
    	}

		@Test
		@DisplayName("elimina dieta que no existe")
		public void eliminaDietaInexistente() {
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
        @DisplayName("actualiza una dieta correctamente")
        public void actualizaDietaCorrectamente() {
            var dieta = DietaDTO.builder()
                                .nombre("Actualizado")
                                .descripcion("Actualizado")
                                .observaciones("Actualizado")
                                .objetivo("Actualizado")
                                .duracionDias(30)
                                .recomendaciones("Actualizado")
                                .build();
            //var peticion = put("http", "localhost", port, "/dieta/1", dieta);
            HttpHeaders headers = getHeaders();
            HttpEntity<DietaDTO> entity = new HttpEntity<>(dieta, headers);

            // Construye la URI para la solicitud
            var peticion = put("http", "localhost", port, "/dieta/2", dieta);
			ResponseEntity<DietaDTO> respuesta = restTemplate.exchange(peticion, DietaDTO.class);	
			
				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			
            
        }

		@Test
		@DisplayName("devuelve la lista de dietas de un entrenador")
		public void devuelveListaDeDietasDeEntrenador() {
			// Simula la existencia de un entrenador con dietas asociadas
			Long entrenadorId = 1L;
			// Realiza la petición para obtener la lista de dietas de un entrenador
			var peticion = get("http", "localhost", port, "/dieta?entrenador="+entrenadorId);
			var respuesta = restTemplate.exchange(peticion, new ParameterizedTypeReference<List<DietaDTO>>() {});

			// Verifica que se recibe una respuesta exitosa y la lista no está vacía
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isNotEmpty();
		}

		@Test
		@DisplayName("devuelve la dieta asociada a un cliente")
		public void devuelveDietaDeCliente() {
			// Simula la existencia de un cliente con una dieta asociada
			Long clienteId = 1L;
			// Realiza la petición para obtener la dieta asociada a un cliente
			var peticion = get("http", "localhost", port, "/dieta?cliente="+clienteId);
			var respuesta = restTemplate.exchange(peticion, DietaDTO.class);

			// Verifica que se recibe una respuesta exitosa y la dieta es la esperada
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isNotNull();
			// Verifica propiedades de la dieta obtenida si es necesario
		}

		//------------------------------------------

		/* 
		@Test
		@DisplayName("elimina dieta que no existe")
		public void eliminaDietaInexistente(){
			var peticion = delete("http", "localhost", port, "/dieta/3");
			var respuesta = restTemplate.exchange(peticion, void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		
		*/

	}

}
