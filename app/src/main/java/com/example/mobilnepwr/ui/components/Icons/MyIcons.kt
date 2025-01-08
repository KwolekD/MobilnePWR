package com.example.mobilnepwr.ui.components.Icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Sensor_door: ImageVector
    get() {
        if (_Sensor_door != null) {
            return _Sensor_door!!
        }
        _Sensor_door = ImageVector.Builder(
            name = "Sensor_door",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 960f,
            viewportHeight = 960f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1.0f,
                stroke = null,
                strokeAlpha = 1.0f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1.0f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(160f, 880f)
                verticalLineToRelative(-720f)
                quadToRelative(0f, -33f, 23.5f, -56.5f)
                reflectiveQuadTo(240f, 80f)
                horizontalLineToRelative(480f)
                quadToRelative(33f, 0f, 56.5f, 23.5f)
                reflectiveQuadTo(800f, 160f)
                verticalLineToRelative(720f)
                close()
                moveToRelative(80f, -80f)
                horizontalLineToRelative(480f)
                verticalLineToRelative(-640f)
                horizontalLineTo(240f)
                close()
                moveToRelative(380f, -260f)
                quadToRelative(25f, 0f, 42.5f, -17.5f)
                reflectiveQuadTo(680f, 480f)
                reflectiveQuadToRelative(-17.5f, -42.5f)
                reflectiveQuadTo(620f, 420f)
                reflectiveQuadToRelative(-42.5f, 17.5f)
                reflectiveQuadTo(560f, 480f)
                reflectiveQuadToRelative(17.5f, 42.5f)
                reflectiveQuadTo(620f, 540f)
                moveTo(240f, 800f)
                verticalLineToRelative(-640f)
                close()
            }
        }.build()
        return _Sensor_door!!
    }

private var _Sensor_door: ImageVector? = null
