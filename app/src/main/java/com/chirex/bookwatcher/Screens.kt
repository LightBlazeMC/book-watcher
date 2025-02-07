package com.chirex.bookwatcher

sealed class Screens(val route : String) {
    object MenuScreen : Screens("MainMenu")
    object AddEntryScreen : Screens("addEntry")
    object ViewEntriesScreen : Screens("viewEntries")
    object EditEntryScreen : Screens("editEntry")
    object DeleteEntryScreen : Screens("deleteEntry")
    object LogInScreenScreen : Screens("loginScreen")
    object SignUpScreenScreen : Screens("signupScreen")
}