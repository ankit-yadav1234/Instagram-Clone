package com.example.Models

class Reel {
    val thumbnailUrl: String = ""  // ✅ Firestore में saved thumbnail की URL
    val videoUrl: String = ""
    val uploaderId: String = ""
    val description: String = ""
    var reelUrl: String = ""
    var caption: String = ""

    constructor(videoUrl: String, uploaderId: String, timestamp: Long, caption: String)  // empty constructor required by Firebase

    constructor(reelUrl: String, caption: String) {
        this.reelUrl = reelUrl
        this.caption = caption


    }
}