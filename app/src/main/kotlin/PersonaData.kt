import java.io.Serializable

data class PersonaData(
    val nombre: String,
    val aPaterno: String,
    val aMaterno: String,
    val percepcionMensual: String,
    val ocupacion: String,
    val correo: String,
    val telefono: String,
    val curp: String
) : Serializable
