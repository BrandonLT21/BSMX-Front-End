import java.io.Serializable

data class PersonaNacimientoData(
    val estado: String,
    val municipio: String,
    val localidad: String,
    val fechaNacimiento: String
): Serializable
