package com.syartecvisionframeprocessors

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin;
import com.mrousavy.camera.frameprocessor.FrameProcessorPluginRegistry;


class SyartecVisionFrameProcessorsPackage : ReactPackage {
  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        FrameProcessorPluginRegistry.addFrameProcessorPlugin("vision_camera_ocr",
            FrameProcessorPluginRegistry.PluginInitializer { options: Map<String?, Any?>? -> OCRFrameProcessorPlugin() })
        FrameProcessorPluginRegistry.addFrameProcessorPlugin("vision_camera_barcode_scanner2",
            FrameProcessorPluginRegistry.PluginInitializer { options: Map<String?, Any?>? -> VisionCameraCodeScannerPlugin() })
        return emptyList()
  }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return emptyList()
  }
}
