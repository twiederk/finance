package com.d20charactersheet.finance.gui

import androidx.compose.desktop.Window
import androidx.compose.ui.unit.IntSize
import com.d20charactersheet.finance.gui.theme.FinanceTheme
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class MainWindow(
    @Autowired private val categoryList: CategoryList
) {

    fun show() {
        Window(title = "Finance App", size = IntSize(600, 800)) {
            FinanceTheme {
                categoryList.displayCategories()
            }
        }
    }

}



