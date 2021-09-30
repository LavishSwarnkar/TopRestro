package com.lavish.toprestro.old.models

data class Profile(
        var name: String? = null,
        var emailId: String? = null) {

    constructor() : this(name = null)

}