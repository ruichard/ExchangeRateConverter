import com.exchangerate.converter.data.remote.model.ExchangeRateResponse

object SampleData {

    val exchangeRateResponse = ExchangeRateResponse(
        "USD", mapOf(
            "AED" to 3.672475,
            "JPY" to 110.0,
            "CNY" to 7.8,
            "EUR" to 0.85
        )
    )

}