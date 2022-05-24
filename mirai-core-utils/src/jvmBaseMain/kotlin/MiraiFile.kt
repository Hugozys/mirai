/*
 * Copyright 2019-2022 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

package net.mamoe.mirai.utils

import io.ktor.utils.io.core.*
import io.ktor.utils.io.streams.*
import java.io.File

public actual interface MiraiFile {
    public actual val name: String
    public actual val parent: MiraiFile?

    public actual val absolutePath: String

    public actual val length: Long

    public actual val isFile: Boolean
    public actual val isDirectory: Boolean

    public actual fun exists(): Boolean

    public actual fun resolve(path: String): MiraiFile
    public actual fun resolve(file: MiraiFile): MiraiFile

    public actual fun createNewFile(): Boolean
    public actual fun delete(): Boolean

    public actual fun mkdir(): Boolean
    public actual fun mkdirs(): Boolean

    public actual fun input(): Input
    public actual fun output(): Output

    public actual companion object {
        public actual fun create(absolutePath: String): MiraiFile {
            return File(absolutePath).asMiraiFile()
        }
    }
}

public fun File.asMiraiFile(): MiraiFile {
    return JvmFileAsMiraiFile(this)
}

public fun MiraiFile.toJvmFile(): File {
    if (this is JvmFileAsMiraiFile) {
        return jvmFile
    }
    return File(absolutePath)
}

internal class JvmFileAsMiraiFile(
    internal val jvmFile: File
) : MiraiFile {
    override val name: String get() = jvmFile.name
    override val parent: MiraiFile? get() = jvmFile.parentFile?.asMiraiFile()
    override val absolutePath: String get() = jvmFile.absolutePath
    override val length: Long get() = jvmFile.length()
    override val isFile: Boolean get() = jvmFile.isFile
    override val isDirectory: Boolean get() = jvmFile.isDirectory
    override fun exists(): Boolean = jvmFile.exists()
    override fun resolve(path: String): MiraiFile = jvmFile.resolve(path).asMiraiFile()
    override fun resolve(file: MiraiFile): MiraiFile = jvmFile.resolve(file.absolutePath).asMiraiFile()
    override fun createNewFile(): Boolean = jvmFile.createNewFile()
    override fun delete(): Boolean = jvmFile.delete()
    override fun mkdir(): Boolean = jvmFile.mkdir()
    override fun mkdirs(): Boolean = jvmFile.mkdirs()
    override fun input(): Input = jvmFile.inputStream().asInput()
    override fun output(): Output = jvmFile.outputStream().asOutput()
}
