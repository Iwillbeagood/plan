package jun.money.mate.model.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
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
