//
//  FrameProcessorLoader.m
//  zLogistics
//
//  Created by Mac-de-Eric on 26/09/2023.
//

#import <Foundation/Foundation.h>
#import <VisionCamera/FrameProcessorPlugin.h>
#import <VisionCamera/FrameProcessorPluginRegistry.h>
#import "syartec_vision_frame_processors-Swift.h"


VISION_EXPORT_SWIFT_FRAME_PROCESSOR(VisionCameraCodeScanner, vision_camera_barcode_scanner2)
VISION_EXPORT_SWIFT_FRAME_PROCESSOR(OCRFrameProcessorPlugin, vision_camera_ocr)
