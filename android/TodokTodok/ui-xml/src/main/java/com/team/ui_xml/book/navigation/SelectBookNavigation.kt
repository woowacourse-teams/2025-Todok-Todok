package com.team.ui_xml.book.navigation

import android.app.Activity
import com.team.core.navigation.SelectBookRoute
import com.team.ui_xml.book.SelectBookActivity
import javax.inject.Inject

class SelectBookNavigation
    @Inject
    constructor() : SelectBookRoute {
        override fun navigateToSelectBook(fromActivity: Activity) {
            val intent = SelectBookActivity.Intent(fromActivity)
            fromActivity.startActivity(intent)
        }
    }
