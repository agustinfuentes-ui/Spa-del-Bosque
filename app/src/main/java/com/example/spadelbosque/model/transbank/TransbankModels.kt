package com.example.spadelbosque.model.transbank

data class TransbankCreateRequest(
    val buy_order: String,
    val session_id: String,
    val amount: Int,             // Monto en CLP
    val return_url: String       // URL de retorno
)

data class TransbankCreateResponse(
    val token: String,
    val url: String
)

data class TransbankCommitResponse(
    val vci: String?,
    val amount: Int,
    val status: String,
    val buy_order: String,
    val session_id: String,
    val card_detail: CardDetail?,
    val accounting_date: String?,
    val transaction_date: String?,
    val authorization_code: String?,
    val payment_type_code: String?,
    val response_code: Int,
    val installments_number: Int?
)

data class CardDetail(
    val card_number: String
)

enum class EstadoTransbank {
    INICIANDO,
    EN_PROCESO,
    AUTORIZADO,
    RECHAZADO,
    ERROR
}