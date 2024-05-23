package ciklumtechledeus.entidades;

import java.net.URI;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import ciklumtechledeus.entidades.dtos.DietaDTO;
import ciklumtechledeus.entidades.entities.Dieta;
import ciklumtechledeus.entidades.repositories.DietaRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de dietas")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DietasApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value = "${local.server.port}")
	private int port;

	@Autowired
	private DietaRepository repoDieta;

	@BeforeEach
	public void initializeDatabase() {
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


	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.get(uri)
            .accept(MediaType.APPLICATION_JSON)
            .build();
        return peticion;
    }

	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.delete(uri)
            .build();
        return peticion;
    }

	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.post(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .body(object);
        return peticion;
    }

	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.put(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .body(object);
        return peticion;
    }

	private void compruebaCampos(Dieta expected, Dieta actual){
		assertThat(actual.getId()).isEqualTo(expected.getId());
//		assertThat(actual.getClienteId()).isEqualTo(expected.getClienteId());
//		assertThat(actual.getEntrenadorId()).isEqualTo(expected.getClienteId());
	}

	@Test
	void contextLoads() {
	}

	@Nested
	@DisplayName("cuando no hay dietas")
	public class BaseDeDatosSinDatos{

		@Test
		@DisplayName("devuelve la lista de dietas")
		public void devuelveDietas() {
			var peticion = get("http", "localhost", port, "/dieta");

			var respuesta = restTemplate.exchange(peticion,
				new ParameterizedTypeReference<Set<Dieta>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().isEmpty());
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

		@Test
		@DisplayName("devuelve error al eliminar una dieta que no existe")
		public void eliminarDietaInexistente(){
			var peticion = delete("http", "localhost", port, "/dieta/1");
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

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

		
	}


	@Nested 
    @DisplayName("cuando la base de datos contiene datos")
    public class BaseDeDatosConDatos {
	
		@BeforeEach
		public void insertarDatos(){
			var dieta1 = new Dieta();
			dieta1.setId(1L);
			dieta1.setNombre("Dieta 1");
			dieta1.setDescripcion("Dieta para definición");
			dieta1.setObservaciones("15000 pasos diarios");
			dieta1.setObjetivo("1800 kcal al dia");
			dieta1.setDuracionDias(30);
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
		public void eliminaDietaCorrectamente(){
			var peticion = delete("http", "localhost", port, "/dieta/1");
			var respuesta = restTemplate.exchange(peticion, Void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

		}

		@Test
		@DisplayName("elimina dieta que no existe")
		public void eliminaDietaInexistente(){
			var peticion = delete("http", "localhost", port, "/dieta/3");
			var respuesta = restTemplate.exchange(peticion, void.class);
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}




	}

}
