package com.syartecvisionframeprocessors

import android.annotation.SuppressLint
import android.graphics.Point
import android.graphics.Rect
import android.media.Image
import android.util.ArrayMap
import android.util.Log
import androidx.camera.core.ImageProxy
import com.facebook.react.bridge.ReadableNativeMap
import com.facebook.react.bridge.WritableNativeArray
import com.facebook.react.bridge.WritableNativeMap
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mrousavy.camera.frameprocessor.Frame
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin
import com.mrousavy.camera.parsers.Orientation

class OCRFrameProcessorPlugin: FrameProcessorPlugin() {

    private fun getBlockArray(blocks: MutableList<Text.TextBlock>): ArrayList<Any> {
        val blockArray = ArrayList<Any>()

        for (block in blocks) {
            val blockMap = HashMap<String, Any?>()

            blockMap.put("text", block.text)
            blockMap.put("recognizedLanguages", getRecognizedLanguages(block.recognizedLanguage))
            blockMap.put("cornerPoints", block.cornerPoints?.let { getCornerPoints(it) })
            blockMap.put("frame", getFrame(block.boundingBox))
            blockMap.put("lines", getLineArray(block.lines))

            blockArray.add(blockMap)
        }
        return blockArray
    }

    private fun getLineArray(lines: MutableList<Text.Line>): ArrayList<Any> {
        val lineArray = ArrayList<Any>()

        for (line in lines) {
            val lineMap = HashMap<String, Any?>()

            lineMap.put("text", line.text)
            lineMap.put("recognizedLanguages", getRecognizedLanguages(line.recognizedLanguage))
            lineMap.put("cornerPoints", line.cornerPoints?.let { getCornerPoints(it) })
            lineMap.put("frame", getFrame(line.boundingBox))
            lineMap.put("elements", getElementArray(line.elements))

            lineArray.add(lineMap)
        }
        return lineArray
    }

    private fun getElementArray(elements: MutableList<Text.Element>): ArrayList<Any> {
        val elementArray = ArrayList<Any>()

        for (element in elements) {
            val elementMap = HashMap<String, Any?>()

            elementMap.put("text", element.text)
            elementMap.put("cornerPoints", element.cornerPoints?.let { getCornerPoints(it) })
            elementMap.put("frame", getFrame(element.boundingBox))
        }
        return elementArray
    }

    private fun getRecognizedLanguages(recognizedLanguage: String): ArrayList<Any> {
        val recognizedLanguages = ArrayList<Any>()
        recognizedLanguages.add(recognizedLanguage)
        return recognizedLanguages
    }

    private fun getCornerPoints(points: Array<Point>): ArrayList<Any> {
        val cornerPoints = ArrayList<Any>()

        for (point in points) {
            val pointMap = HashMap<String, Any>()
            pointMap.put("x", point.x)
            pointMap.put("y", point.y)
            cornerPoints.add(pointMap)
        }
        return cornerPoints
    }

    private fun getFrame(boundingBox: Rect?): HashMap<String, Any> {
        val frame = HashMap<String, Any>()

        if (boundingBox != null) {
            frame.put("x", boundingBox.exactCenterX().toDouble())
            frame.put("y", boundingBox.exactCenterY().toDouble())
            frame.put("width", boundingBox.width())
            frame.put("height", boundingBox.height())
            frame.put("boundingCenterX", boundingBox.centerX())
            frame.put("boundingCenterY", boundingBox.centerY())
        }
        return frame
    }

    override fun callback(frame: Frame, params: MutableMap<String, Any>?): Any? {

        val result = HashMap<String, Any>()

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        @SuppressLint("UnsafeOptInUsageError")
        val mediaImage: Image? = frame.getImage()

        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, Orientation.fromUnionValue(frame.orientation)!!.toDegrees())
            Log.w("LOG_FRAME", frame.pixelFormat)
            val task: Task<Text> = recognizer.process(image)
            try {
                val text: Text = Tasks.await<Text>(task)
                result.put("text", text.text)
                result.put("blocks", getBlockArray(text.textBlocks))
            } catch (e: Exception) {
                return null
            }
        }

        val data = HashMap<String, Any>()
        data.put("result", result)
        return data
    }


}