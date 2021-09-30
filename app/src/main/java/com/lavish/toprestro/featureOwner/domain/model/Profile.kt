package com.lavish.toprestro.featureOwner.domain.model

data class Profile(
        var name: String? = null,
        var emailId: String? = null) {

    constructor() : this(name = null)

}