package com.lavish.toprestro.featureOwner.domain.model

data class Profile(
    val name: String,
    val emailId: String) {
    constructor(): this("", "")
}