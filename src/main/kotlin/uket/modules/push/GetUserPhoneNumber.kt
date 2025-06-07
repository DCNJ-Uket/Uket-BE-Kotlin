package uket.uket.modules.push

fun interface GetUserPhoneNumber {
    fun invoke(userId: Long): String
}
