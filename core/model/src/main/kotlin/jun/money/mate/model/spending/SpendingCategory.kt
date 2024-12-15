package jun.money.mate.model.spending

sealed interface SpendingCategory {

    data object NotSelected : SpendingCategory

    data class CategoryType(val type: SpendingCategoryType) : SpendingCategory

    companion object {

        const val NOT_SELECTED = "NotSelected"
        val ETC = CategoryType(SpendingCategoryType.기타)

        fun SpendingCategory.name(): String {
            return when (this) {
                is CategoryType -> type.name
                NotSelected -> "카테고리 선택"
            }
        }

        fun SpendingCategory.type(): SpendingCategoryType {
            return when (this) {
                is CategoryType -> type
                NotSelected -> SpendingCategoryType.기타
            }
        }

        fun find(name: String): CategoryType {
            val categoryType = SpendingCategoryType.entries.find { it.name == name } ?: SpendingCategoryType.기타
            return CategoryType(categoryType)
        }
    }
}

enum class SpendingCategoryType(val isSubscribe: Boolean = false) {
    교통비,
    교육,
    보험,
    통신비,
    주거비,
    관리비,
    운동,
    할부,
    렌트비,
    기타,

    넷플릭스(true),
    유튜브(true),
    디즈니플러스(true),
    아마존프라임(true),
    왓챠(true),
    웨이브(true),
    티빙(true),
    쿠팡(true),
    멜론(true),
    스포티파이(true),
    네이버플러스(true)

    ;

    companion object {
        val normals = entries.filter { !it.isSubscribe }
        val subscribes = entries.filter { it.isSubscribe }

        fun values(isSubscribe: Boolean) = if (isSubscribe) subscribes else normals
    }
}