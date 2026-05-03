package com.satwik.sbslaunchpad.features.completeprofile

sealed class DocumentType(val folderName: String) {
    data object Resume : DocumentType("resume")
    data object TenthMarksheet : DocumentType("marksheet/tenth")
    data object TwelfthMarksheet : DocumentType("marksheet/twelfth")
    data object ProfilePic : DocumentType("profile_pic")
}
