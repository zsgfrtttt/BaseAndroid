package com.hydbest.baseandroid.activity.anim.scene

class Item(name: String?, author: String?, fileName: String?) {
    private var mName: String? = null
    private var mAuthor: String? = null
    private var mFileName: String? = null

    init {
        mName = name
        mAuthor = author
        mFileName = fileName
    }

    companion object {
        private val LARGE_BASE_URL = "https://storage.googleapis.com/androiddevelopers/sample_data/activity_transition/large/"
        private val THUMB_BASE_URL = "https://storage.googleapis.com/androiddevelopers/sample_data/activity_transition/thumbs/"

        val ITEMS: Array<Item> = arrayOf<Item>(
                Item("Flying in the Light", "Romain Guy", "flying_in_the_light.jpg"),
                Item("Caterpillar", "Romain Guy", "caterpillar.jpg"),
                Item("Look Me in the Eye", "Romain Guy", "look_me_in_the_eye.jpg"),
                Item("Flamingo", "Romain Guy", "flamingo.jpg"),
                Item("Rainbow", "Romain Guy", "rainbow.jpg"),
                Item("Over there", "Romain Guy", "over_there.jpg"),
                Item("Jelly Fish 2", "Romain Guy", "jelly_fish_2.jpg"),
                Item("Lone Pine Sunset", "Romain Guy", "lone_pine_sunset.jpg"))

        fun getItem(id: Long): Item? {
            for (item in ITEMS) {
                if (item.getId() == id) {
                    return item
                }
            }
            return null
        }

    }

    fun getId(): Long {
        return (mName.hashCode() + mFileName.hashCode()).toLong()
    }

    fun getAuthor(): String? {
        return mAuthor
    }

    fun getName(): String? {
        return mName
    }

    fun getPhotoUrl(): String? {
        return LARGE_BASE_URL + mFileName
    }

    fun getThumbnailUrl(): String? {
        return THUMB_BASE_URL + mFileName
    }
}