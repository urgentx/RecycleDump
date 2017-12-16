package com.urgentx.recycledump

import android.content.Context

fun generateCategories(context: Context): Array<String>{
    val category1 = "${context.getString(R.string.recycle_info_category1)} - ${context.getString(R.string.recycle_info_category1_desc)}"
    val category2 = "${context.getString(R.string.recycle_info_category2)} - ${context.getString(R.string.recycle_info_category2_desc)}"
    val category3 = "${context.getString(R.string.recycle_info_category3)} - ${context.getString(R.string.recycle_info_category3_desc)}"
    val category4 = "${context.getString(R.string.recycle_info_category4)} - ${context.getString(R.string.recycle_info_category4_desc)}"
    val category5 = "${context.getString(R.string.recycle_info_category5)} - ${context.getString(R.string.recycle_info_category5_desc)}"
    val category6 = "${context.getString(R.string.recycle_info_category6)} - ${context.getString(R.string.recycle_info_category6_desc)}"
    val category7 = "${context.getString(R.string.recycle_info_category7)} - ${context.getString(R.string.recycle_info_category7_desc)}"
    val category8 = "${context.getString(R.string.recycle_info_category8)} - ${context.getString(R.string.recycle_info_category8_desc)}"
    val category9 = "${context.getString(R.string.recycle_info_category9)} - ${context.getString(R.string.recycle_info_category9_desc)}"
    val category10 = "${context.getString(R.string.recycle_info_category10)} - ${context.getString(R.string.recycle_info_category10_desc)}"
    val category11 = "${context.getString(R.string.recycle_info_category11)} - ${context.getString(R.string.recycle_info_category11_desc)}"
    val category12 = "${context.getString(R.string.recycle_info_category12)} - ${context.getString(R.string.recycle_info_category12_desc)}"

    return arrayOf(category1,
            category2,
            category3,
            category4,
            category5,
            category6,
            category7,
            category8,
            category9,
            category10,
            category11,
            category12)
}

fun generateCategoryNames(context: Context): Array<String>{

    val category1 = context.getString(R.string.recycle_info_category1)
    val category2 = context.getString(R.string.recycle_info_category2)
    val category3 = context.getString(R.string.recycle_info_category3)
    val category4 = context.getString(R.string.recycle_info_category4)
    val category5 = context.getString(R.string.recycle_info_category5)
    val category6 = context.getString(R.string.recycle_info_category6)
    val category7 = context.getString(R.string.recycle_info_category7)
    val category8 = context.getString(R.string.recycle_info_category8)
    val category9 = context.getString(R.string.recycle_info_category9)
    val category10 = context.getString(R.string.recycle_info_category10)
    val category11 = context.getString(R.string.recycle_info_category11)
    val category12 = context.getString(R.string.recycle_info_category12)

    return arrayOf(category1,
            category2,
            category3,
            category4,
            category5,
            category6,
            category7,
            category8,
            category9,
            category10,
            category11,
            category12)
}

val debugTagCreateCollector = "createCollector"