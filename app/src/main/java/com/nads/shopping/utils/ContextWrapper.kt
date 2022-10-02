package com.nads.shopping.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.*


class ContextWrapper(base: Context?) : ContextWrapper(base) {

  companion object{
      fun wrap(
          context: Context,
          newLocale: Locale
      ): ContextWrapper {
          var context = context
          val res: Resources = context.resources
          val configuration: Configuration = res.configuration
          when {
              Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                  configuration.setLocale(newLocale)
                  val localeList = LocaleList(newLocale)
                  LocaleList.setDefault(localeList)
                  configuration.setLocales(localeList)
                  context = context.createConfigurationContext(configuration)
              }
              Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                  configuration.setLocale(newLocale)
                  context = context.createConfigurationContext(configuration)
              }
          }
          return ContextWrapper(context)
      }
  }
}