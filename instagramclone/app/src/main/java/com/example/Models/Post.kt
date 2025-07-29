package com.example.Models

class Post {
    var imageUrl: String = ""
    var caption: String = ""

    constructor()  // empty constructor required by Firebase

    constructor(imageUrl: String, caption: String) {
        this.imageUrl = imageUrl
        this.caption = caption
    }
}
