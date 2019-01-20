package com.cristian.cardoso.grintest.models.commands

sealed class MainViewModelCommands {

    object GoToAllDevicestActivity : MainViewModelCommands()
    class Error(val errorString: String): MainViewModelCommands()
    object TurnOnBluetooth : MainViewModelCommands()
    object RequestLocationPermission : MainViewModelCommands()
}