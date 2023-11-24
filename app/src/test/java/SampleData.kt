import com.exchangerate.converter.data.remote.model.ExchangeRateResponse

object SampleData {

    val exchangeRateResponse = ExchangeRateResponse(
        "USD", mapOf(
            "AED" to 3.672475,
            "JPY" to 69.085468,
            "CNY" to 7.8,
            "ERU" to 322.0
        )
    )

}