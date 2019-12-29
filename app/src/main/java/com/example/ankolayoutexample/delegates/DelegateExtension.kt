package com.example.ankolayoutexample.delegates

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> Bundle.put(key : String, value : T){
    when(value){
        is Boolean-> putBoolean(key , value)
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Short -> putShort(key, value)
        is Long -> putLong(key, value)
        is Byte -> putByte(key, value)
        is ByteArray -> putByteArray(key, value)
        is Char -> putChar(key, value)
        is CharArray -> putCharArray(key, value)
        is CharSequence -> putCharSequence(key, value)
        is Float -> putFloat(key, value)
        is Bundle -> putBundle(key, value)
        is Parcelable -> putParcelable(key, value)
        is Serializable -> putSerializable(key, value)
        else -> throw IllegalStateException("Type of property $key is not supported")
    }
}

fun <T : Any> argument() : ReadWriteProperty<Fragment,T> = FragmentArgumentDelegate()

fun <T : Any> argumentNullable() : ReadWriteProperty<Fragment , T?> = FragmentNullableArgumentDelegate()

fun SharedPreferences.string(defaultValue : String="", key : (KProperty<*>)-> String = KProperty<*>::name) :
        ReadWriteProperty<Any , String> = object  : ReadWriteProperty<Any, String>{
    override fun getValue(thisRef: Any, property: KProperty<*>) = getString(key(property) , defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) =
        edit().putString(key(property) , value).apply()

}

fun SharedPreferences.stringNullable(defaultValue : String?=null, key : (KProperty<*>)-> String = KProperty<*>::name) :
        ReadWriteProperty<Any , String?> = object  : ReadWriteProperty<Any, String?>{
    override fun getValue(thisRef: Any, property: KProperty<*>) = getString(key(property) , defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) =
        edit().putString(key(property) , value).apply()

}

fun SharedPreferences.int(defaultValue : Int=0, key : (KProperty<*>)-> String = KProperty<*>::name) :
        ReadWriteProperty<Any , Int> = object  : ReadWriteProperty<Any, Int>{
    override fun getValue(thisRef: Any, property: KProperty<*>) = getInt(key(property) , defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) =
        edit().putInt(key(property) , value).apply()

}

fun SharedPreferences.boolean(defaultValue : Boolean=false, key : (KProperty<*>)-> String = KProperty<*>::name) :
        ReadWriteProperty<Any , Boolean> = object  : ReadWriteProperty<Any, Boolean>{
    override fun getValue(thisRef: Any, property: KProperty<*>) = getBoolean(key(property) , defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) =
        edit().putBoolean(key(property) , value).apply()


}

fun SharedPreferences.long(defaultValue : Long=0, key : (KProperty<*>)-> String = KProperty<*>::name) :
        ReadWriteProperty<Any , Long> = object  : ReadWriteProperty<Any, Long>{
    override fun getValue(thisRef: Any, property: KProperty<*>) = getLong(key(property) , defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) =
        edit().putLong(key(property) , value).apply()


}

fun SharedPreferences.float(defaultValue : Float=0f, key : (KProperty<*>)-> String = KProperty<*>::name) :
        ReadWriteProperty<Any , Float> = object  : ReadWriteProperty<Any, Float>{
    override fun getValue(thisRef: Any, property: KProperty<*>) = getFloat(key(property) , defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) =
        edit().putFloat(key(property) , value).apply()


}

