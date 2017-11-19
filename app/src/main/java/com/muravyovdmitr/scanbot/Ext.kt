package com.muravyovdmitr.scanbot

import android.app.Activity
import kotlin.reflect.KClass


/**
 * Created by Dima Muravyov on 18.11.2017.
 */
fun Activity.startActivity(`class`: KClass<*>) = startActivity(`class`.java)

fun Activity.startActivity(`class`: Class<*>) = startActivity(android.content.Intent(this, `class`))