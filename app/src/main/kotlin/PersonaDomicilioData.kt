import java.io.Serializable

data class PersonaDomicilioData(
    val colonia: String,
    val codigoPostal: String,
    val estado: String,
    val numInterior: String,
    val numExterior: String,
    val municipio: String,
    val calle: String,
    val localidad: String

): Serializable
