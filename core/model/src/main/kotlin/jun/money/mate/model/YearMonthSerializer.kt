package jun.money.mate.model

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.time.YearMonth
import java.time.format.DateTimeFormatter

object YearMonthSerializer : KSerializer<YearMonth> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM") // `YYYY-MM` 형식

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("YearMonth", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: YearMonth) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): YearMonth {
        val stringValue = decoder.decodeString()
        return YearMonth.parse(stringValue, formatter)
    }
}
