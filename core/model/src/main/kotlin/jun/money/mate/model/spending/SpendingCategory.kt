package jun.money.mate.model.spending

sealed interface SpendingCategory {

    data object NotSelected : SpendingCategory

    data class CategoryType(val type: SpendingCategoryType) : SpendingCategory

    companion object {

        fun SpendingCategory.name(): String {
            return when (this) {
                is CategoryType -> type.name
                NotSelected -> "카테고리 선택"
            }
        }

        fun find(title: String): SpendingCategory {
            val categoryType = SpendingCategoryType.entries.find { it.name == title } ?: SpendingCategoryType.기타
            return CategoryType(categoryType)
        }
    }
}

enum class SpendingCategoryType {
    교통비,
    교육,
    보험,
    통신비,
    주거비,
    관리비,
    구독료,
    운동,
    할부,
    렌트비,
    기타;
}