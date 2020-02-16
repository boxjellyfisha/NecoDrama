package com.kellyhong.necodrama.http

import android.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.kellyhong.necodrama.http.response.BaseResponse
import java.io.IOException

/**
 * Define your custom gson type adapter.
 */

class GsonTypeAdapterFactory : TypeAdapterFactory {

	override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
		/*
		[Example 1: use newTypeErrorHandleInstance]
		if (targetType.isAssignableFrom(TARGET_CLASS.class))
			return newTypeErrorHandleInstance(TARGET_CLASS.class, IS_JSON_OBJECT_OR_NOT);

		[Example 2: use other TypeAdapter]
		if (targetType.isAssignableFrom(TARGET_CLASS.class))
			return new YOUR_TYPE_ADAPTER();
		*/

		val targetType = type.rawType

		return if(targetType.isAssignableFrom(Long::class.java) || targetType.isAssignableFrom(Long::class.javaPrimitiveType!!))
		// register to avoid that use long adapter to read empty string.
			newLongTypeAdapter() as TypeAdapter<T>?
		else if(targetType.isAssignableFrom(Int::class.java) || targetType.isAssignableFrom(Int::class.javaPrimitiveType!!))
			newNumberTypeAdapter(Int::class.java) as TypeAdapter<T>?
		else if(targetType.isAssignableFrom(Float::class.java) || targetType.isAssignableFrom(Float::class.javaPrimitiveType!!))
			newNumberTypeAdapter(Float::class.java) as TypeAdapter<T>?
		else if(targetType.isAssignableFrom(Double::class.java) || targetType.isAssignableFrom(Double::class.javaPrimitiveType!!))
			newNumberTypeAdapter(Double::class.java) as TypeAdapter<T>?
		else if(targetType.isAssignableFrom(BaseResponse::class.java))
			newTypeErrorHandleInstance(BaseResponse::class.java, true) as TypeAdapter<T>?
//		else if(targetType.simpleName == "JsonWrapList") {
//			newJWListHandleInstance(
//					type.rawType, (type.type as ParameterizedType).actualTypeArguments[0] as Class<*>) as TypeAdapter<T>?
//		}
		else
			null    // use default
	}

	companion object {
		private val TAG = GsonTypeAdapterFactory::class.java.simpleName

		/**
		 * Notice: dataGson only set "excludeFieldsWithoutExposeAnnotation". (not set any TypeAdapter)
		 */
		private fun <D> newTypeErrorHandleInstance(typeOfDataClass: Class<D>, isDataAnObject: Boolean): TypeAdapter<*> {
			return object : TypeAdapter<D>() {
				@Throws(IOException::class)
				override fun write(out: JsonWriter, value: D) {
					Log.e(TAG, String.format("TypeAdapter: %s.write(): do nothing", typeOfDataClass.simpleName))
				}

				@Throws(IOException::class)
				override fun read(`in`: JsonReader): D? {
					val targetJsonToken = if(isDataAnObject)
						JsonToken.BEGIN_OBJECT
					else
						JsonToken.BEGIN_ARRAY
					return if(`in`.peek() == targetJsonToken) {
						EndPointGenerator.dataGson.fromJson<D>(`in`, typeOfDataClass)
					} else {
						Log.e(TAG, String.format("read: not a target (%s), path = %s", typeOfDataClass.simpleName, `in`.path))
						`in`.skipValue()
						if(typeOfDataClass == BaseResponse::class.java)
							BaseResponse() as D
						else
							null
					}
				}
			}
		}


		/**
		 * Use this custom TypeAdapter when server return ""(empty string) at long field.
		 * It will parse empty string to null. (long field will set 0L)
		 */
		private fun newLongTypeAdapter(): TypeAdapter<Long>? {
			return object : TypeAdapter<Long>() {
				@Throws(IOException::class)
				override fun write(writer: JsonWriter, value: Long?) {
					if(value == null) {
						writer.nullValue()
						return
					}
					writer.value(value)
				}

				@Throws(IOException::class)
				override fun read(reader: JsonReader): Long? {
					if(reader.peek() == JsonToken.NULL) {
						reader.nextNull()
						return null
					}
					return try {
						java.lang.Long.valueOf(reader.nextString())
					} catch (e: NumberFormatException) {
						null
					}

				}
			}
		}

		private fun newNumberTypeAdapter(N: Class<*>): TypeAdapter<Number>? {
			return object : TypeAdapter<Number>() {
				@Throws(IOException::class)
				override fun write(writer: JsonWriter, value: Number?) {
					if(value == null) {
						writer.nullValue()
						return
					}
					writer.value(value)
				}

				@Throws(IOException::class)
				override fun read(reader: JsonReader): Number? {
					return readNumber(reader, N)
				}
			}
		}

		@Throws(IOException::class)
		private fun readNumber(reader: JsonReader, N: Class<*>): Number? {
			if(reader.peek() == JsonToken.NULL) {
				reader.nextNull()
				return null
			}
			return try {
				when (N) {
					Long::class.java -> java.lang.Long.valueOf(reader.nextString())
					Int::class.java -> Integer.valueOf(reader.nextString())
					Float::class.java -> java.lang.Float.valueOf(reader.nextString())
					Double::class.java -> java.lang.Double.valueOf(reader.nextString())
					else -> null
				}
			} catch (e: NumberFormatException) {
				null
			}
		}
	}
}
