package jun.money.mate.model.save

data class SavingChallenge(
    val title: String,
    val day: Int,
    val amount: Long
) {
    companion object {
        val sample = SavingChallenge(
            title = "적금 1회차",
            day = 1,
            amount = 100_000
        )
    }
}