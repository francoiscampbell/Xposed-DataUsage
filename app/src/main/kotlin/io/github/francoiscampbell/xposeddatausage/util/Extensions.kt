package io.github.francoiscampbell.xposeddatausage.util

import de.robv.android.xposed.callbacks.XC_LayoutInflated

/**
 * Created by francois on 16-03-13.
 */
fun XC_LayoutInflated.LayoutInflatedParam.findViewById(id: String) = view.findViewById(res.getIdentifier(id, "id", res.packageName))
