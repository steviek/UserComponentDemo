package demo.usercomponent.ui

import javax.inject.Inject

class EventNameProvider @Inject constructor() {
    constructor(name: String) : this()
    val eventName = "Droidcon"
}