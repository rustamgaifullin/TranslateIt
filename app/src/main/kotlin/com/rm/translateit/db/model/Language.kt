package com.rm.translateit.db.model

import org.greenrobot.greendao.annotation.*

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "LANGUAGE".
 */
@Entity
class Language {

    @Id(autoincrement = true)
    var id: Long? = null

    /** Not-null value; ensure this value is available before it is saved to the database.  */
    @NotNull
    var code: String? = null
    var name: String? = null

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    @Generated
    constructor() {
    }

    constructor(id: Long?) {
        this.id = id
    }

    @Generated
    constructor(id: Long?, code: String, name: String) {
        this.id = id
        this.code = code
        this.name = name
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
